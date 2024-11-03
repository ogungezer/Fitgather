package com.example.fitgether.presentation.view

import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.fitgether.R
import com.example.fitgether.databinding.FragmentLoginBinding
import com.example.fitgether.databinding.FragmentMapsBinding
import com.example.fitgether.presentation.viewmodel.CreateEventViewmodel
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Locale
import kotlin.math.max
import kotlin.math.min


class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val createEventViewmodel : CreateEventViewmodel by activityViewModels()
    private var mCurrentUser : FirebaseUser? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var googleMap: GoogleMap
    private var chosenLocation : LatLng? = null
    private var chosenAddress : String? = null

    private val TURKEY_BOUNDS = LatLngBounds(
        LatLng(36.0, 26.0), //Güneybatı köşesi
        LatLng(42.0, 45.0)  //Kuzeydoğu köşesi
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapsBinding.inflate(inflater,container,false)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        mCurrentUser = auth.currentUser


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(!Places.isInitialized()){
            Places.initialize(requireContext(), getString(R.string.API_KEY))
        }

        val autoCompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment


        autoCompleteFragment.setCountries("TR")
        autoCompleteFragment.setPlaceFields(listOf(Place.Field.LAT_LNG,Place.Field.NAME,Place.Field.ADDRESS))

        autoCompleteFragment.setOnPlaceSelectedListener(object  : PlaceSelectionListener{
            override fun onError(p0: Status) {
                Log.i("PLACEAPI","An Error Occurred! $p0 ")
            }

            override fun onPlaceSelected(p0: Place) {
                chosenLocation = p0.latLng
                val chosenLat = chosenLocation?.latitude
                val chosenLong = chosenLocation?.longitude
                chosenAddress = p0.address
                googleMap.clear()
                Log.i("PLACEAPI","Place Name: ${p0.name}\nPlace Adress: ${p0.address}\nPlace LatLng: ${p0.latLng}")
                createEventViewmodel.locationControl(true)
                createEventViewmodel.createEvent(location = listOf(chosenLat!!, chosenLong!!), address = chosenAddress )
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(chosenLocation!!, 10f))
                googleMap.addMarker(MarkerOptions().position(chosenLocation!!).title(chosenAddress).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
            }

        })

        return binding.root
    }



    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        with(googleMap.uiSettings){
            isZoomControlsEnabled = true
            isMapToolbarEnabled = true
        }
        googleMap.setLatLngBoundsForCameraTarget(TURKEY_BOUNDS)
        googleMap.setMinZoomPreference(5f)

        //harita ilk açıldığında kullanıcının bulunduğu konum gösterilecek. Eğer konumu yoksa türkiye.
        fetchUserLocation {location ->
            chosenLocation = location
            if(chosenLocation != null){
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chosenLocation!!, 12f))

            }
        }

        googleMap.setOnMapClickListener { location ->
            if(TURKEY_BOUNDS.contains(location)){
                chosenLocation = location
                val chosenLat = chosenLocation?.latitude
                val chosenLong = chosenLocation?.longitude
                googleMap.clear()
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(chosenLocation!!, 18f))
                googleMap.addMarker(MarkerOptions().position(chosenLocation!!).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                getAddressFromLocation(chosenLocation!!){
                    chosenAddress = it
                }
                createEventViewmodel.locationControl(true)
                createEventViewmodel.createEvent(location = listOf(chosenLat!!, chosenLong!!), address = chosenAddress )

            }

        }

        googleMap.setOnMarkerClickListener { marker ->
            marker.title = chosenAddress
            marker.showInfoWindow()
            false
        }

    }



    //Map'a tıklayarak eklediğimiz markerin lat long bilgileri ile geocoder sınıfını kullanarak adrese erişmek için kullanıldı.
    private fun getAddressFromLocation(location: LatLng, callbackAddress : (String) -> Unit) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            if(Build.VERSION.SDK_INT > 33){
                geocoder.getFromLocation(location.latitude,location.longitude,1) {addresses->
                    if (addresses.isNotEmpty()) {
                        val mAddresses = addresses[0]
                        val addressText = mAddresses.getAddressLine(0)
                        callbackAddress(addressText)
                        Log.d("MapClick", "Address: $addressText")
                    } else {
                        Log.d("MapClick", "No address found for location")
                    }
                }

            }else{
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val mAddresses = addresses[0]
                    val addressText = mAddresses.getAddressLine(0)
                    callbackAddress(addressText)
                    Log.d("MapClick", "Address: $addressText")
                } else {
                    Log.d("MapClick", "No address found for location")
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("MapClick", "Geocoder failed: ${e.message}")
        }
    }


    private fun fetchUserLocation(callback: (LatLng?) -> Unit) {
        db.collection("Users").document(mCurrentUser?.uid!!)
            .get()
            .addOnSuccessListener { result ->
                val userLocation = result.data?.get("location") as ArrayList<*>
                if (userLocation.isNotEmpty()) {
                    val locationLat = userLocation[0] as Double
                    val locationLong = userLocation[1] as Double
                    val location = LatLng(locationLat, locationLong)
                    callback(location)
                } else {
                    callback(LatLng(39.9334, 32.8597)) //tr
                }
            }
            .addOnFailureListener {
                Log.d("db", "${it.message}")
                callback(LatLng(39.9334, 32.8597)) //tr
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}