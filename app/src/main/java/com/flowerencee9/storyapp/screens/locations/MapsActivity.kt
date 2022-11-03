package com.flowerencee9.storyapp.screens.locations

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flowerencee9.storyapp.R
import com.flowerencee9.storyapp.databinding.ActivityMapsBinding
import com.flowerencee9.storyapp.models.response.Story
import com.flowerencee9.storyapp.screens.detail.DetailActivity
import com.flowerencee9.storyapp.screens.formuploader.FormUploaderActivity
import com.flowerencee9.storyapp.support.customs.CustomInfoWindow
import com.flowerencee9.storyapp.support.supportclass.LoadingStateAdapter
import com.flowerencee9.storyapp.support.supportclass.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    companion object {
        fun newIntent(context: Context) = Intent(context, MapsActivity::class.java)
        private val TAG = MapsActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    private val viewModel: MapsViewModel by viewModels {
        ViewModelFactory(this)
    }

    private val mapItemAdapter: AdapterItemMapsData by lazy {
        AdapterItemMapsData(
            { latLng: LatLng -> relocateScreen(latLng) },
            { story: Story -> detailStory(story) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
        initRequestData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maps_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_drawer -> {
                binding.drawerLayout.openDrawer(GravityCompat.END)
                true
            }
            else -> true
        }
    }

    private fun setupView() {
        binding.btnAdd.isEnabled = false
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.rvItem.adapter = mapItemAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                mapItemAdapter.retry()
            }
        )
        binding.rvItem.layoutManager = LinearLayoutManager(this@MapsActivity)
        setSupportActionBar(binding.mapsToolbar)
    }

    private fun relocateScreen(latLng: LatLng, addNew: Boolean? = false) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, when(addNew){
            true -> 20f
            false -> 15f
            else -> 5f
        }))
        closeDrawer()
        if (addNew == true){
            mMap.addCircle(
                CircleOptions()
                    .center(latLng)
                    .radius(15.0)
                    .strokeColor(R.color.night_fall)
                    .fillColor(R.color.night_fall_transparent)
            )
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(FormUploaderActivity.newIntent(this, latLng))
            }, 2000)
        }
    }

    private fun closeDrawer(){
        if (binding.drawerLayout.isDrawerOpen(binding.navItemList)) binding.drawerLayout.closeDrawer(
            binding.navItemList
        )
    }

    private fun detailStory(story: Story) {
        startActivity(DetailActivity.newIntent(this, story))
    }

    private fun initRequestData() {
        viewModel.stories.observe(this){
            mapItemAdapter.submitData(lifecycle, it)
        }
        Log.d(TAG, "map data ${mapItemAdapter.snapshotData.value}")
        if (mapItemAdapter.snapshotData.value?.isNotEmpty() == true) mapItemAdapter.refresh()
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        with(mMap) {
            uiSettings.apply {
                isZoomControlsEnabled = true
            }

            setOnMarkerClickListener(this@MapsActivity)
            setInfoWindowAdapter(CustomInfoWindow(this@MapsActivity))
        }
        setMapStyle()
        showAllMarker()
        getMyLastLocation()
        showIndonesiaDefault()
    }

    private fun showIndonesiaDefault() {
        val latLng = LatLng(-0.789275, 113.921327)
        relocateScreen(latLng, null)
    }

    private fun showAllMarker() {
        mapItemAdapter.snapshotData.observe(this){
            it.forEach { data ->
                val latLng = LatLng(data.lat!!, data.lon!!)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(data.name)
                        .snippet(data.description)
                )
            }
        }

    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        val markerLatLng = p0.position.let {
            LatLng(it.latitude, it.longitude)
        }
        p0.showInfoWindow()
        relocateScreen(markerLatLng)
        return true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> getMyLastLocation()
                else -> Toast.makeText(this, getString(R.string.permission_not_granted), Toast.LENGTH_SHORT).show()
            }
        }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun getMyLastLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    binding.btnAdd.apply {
                        isEnabled = true
                        setOnClickListener {
                            relocateScreen(currentLatLng, true)
                        }
                    }

                } else {
                    binding.btnAdd.isEnabled = false
                    Toast.makeText(this, getString(R.string.location_not_found), Toast.LENGTH_SHORT)
                        .show()
                }
            }

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

}