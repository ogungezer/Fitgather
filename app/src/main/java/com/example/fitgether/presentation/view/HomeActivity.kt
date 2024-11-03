package com.example.fitgether.presentation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.fitgether.R
import com.example.fitgether.databinding.ActivityHomeBinding
import com.example.fitgether.presentation.viewmodel.HomeActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private val homeActivityViewModel : HomeActivityViewModel by viewModels()
    private var isPermissionGranted = false
    private var shouldCheckPermissionOnResume = false


    override fun onResume() {
        super.onResume()
        if(shouldCheckPermissionOnResume){
            shouldCheckPermissionOnResume = false
            controlUserLocation()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.bottomNavigation.setOnApplyWindowInsetsListener(null)
        binding.bottomNavigation.setPadding(0,0,0,0)

        bottomBarReplaceFragments(HomeScreenFragment()) //init HomeScreen fragment


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this) //for location

        binding.bottomNavigation.setOnItemSelectedListener{item ->
            when(item.itemId){
                R.id.homeBottom -> bottomBarReplaceFragments(HomeScreenFragment())
                R.id.exploreBottom -> bottomBarReplaceFragments(ExploreFragment())
                R.id.calendarBottom -> bottomBarReplaceFragments(CalendarFragment())
                R.id.profileBottom -> bottomBarReplaceFragments(ProfileFragment())
            }
            true
        }
        binding.fab.setOnClickListener {
            isUserLocationNull(auth.currentUser){ isNull ->
                if(isNull){
                    controlUserLocation()
                    println("permission : $isPermissionGranted")
                    if (isPermissionGranted) {
                        binding.bottomNavigation.menu.findItem(R.id.fabBottom).setChecked(true)
                        //navigation to create screen
                        binding.bottomAppBar.visibility = View.GONE
                        binding.fab.visibility = View.GONE
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.containerFragment, CreateEventFragment())
                            addToBackStack(null)
                            commit()
                        }
                    }

                }else{
                    binding.bottomNavigation.menu.findItem(R.id.fabBottom).setChecked(true)
                    //navigation to create screen
                    binding.bottomAppBar.visibility = View.GONE
                    binding.fab.visibility = View.GONE
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.containerFragment, CreateEventFragment())
                        addToBackStack(null)
                        commit()
                    }
                }
            }


        }


        //Kullanıcı back tuşuna bastığında homescreen fragmentine dönmesi için
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val supportFragment = supportFragmentManager
                println("${supportFragment.backStackEntryCount}")
                if(supportFragment.backStackEntryCount > 0){ //aslında 0 olacak ama fab sorunu :)
                    println("back stack count : ${supportFragment.backStackEntryCount}")
                    println("0dan buyuge girdi ")
                    supportFragment.popBackStack()
                    if(supportFragment.backStackEntryCount == 1){
                        println("esittir 2 e  girdi")
                        binding.fab.visibility = View.VISIBLE
                        binding.bottomAppBar.visibility = View.VISIBLE
                        binding.bottomNavigation.menu.findItem(R.id.homeBottom).setChecked(true)
                        bottomBarReplaceFragments(HomeScreenFragment())
                    }
                }
                else{
                    println("baybaya girdi")
                    moveTaskToBack(true)
                }
            }
        })


        homeActivityViewModel.permission.observe(this){
            isPermissionGranted = it
        }


    }


    private fun isUserLocationNull(user : FirebaseUser?, isLocationNull : (Boolean) -> Unit) {
        var userLocation : Any? = emptyArray<Any>()
        user?.let {currentUser ->
            db.collection("Users").document(currentUser.uid)
                .get()
                .addOnSuccessListener {result ->
                    userLocation = result.data?.get("location")
                    println("eski location degeri: $userLocation")
                    if(userLocation == null){
                        isLocationNull(true)
                    }else{
                        isLocationNull(false)
                    }
                }
        }
    }

    //aslında bunu etkinlik oluşturma ve etkinliklere katılma şeylerinde çağırada bilirsin.
    private fun controlUserLocation() {
        isUserLocationNull(auth.currentUser){isNull ->
           if(isNull){
               println("nulmuş")
               if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                   ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                   println("İLK PERME GİRDİ")
                   //rationale kısmı kullanıcı konum iznini reddettiyse snackbar ile istemek için
                   if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                       Snackbar.make(binding.root, "Bunun için konum izni vermelisin!",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver"){
                           ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100)
                       }.show()
                   }else{
                       ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100)
                   }
               } else {
                   println("ikinci perme girdi")
                   fetchLocation()
                   homeActivityViewModel.giveLocationPermission()
               }
           }else{
               println("nul degilmis")
               fetchLocation()
               homeActivityViewModel.giveLocationPermission()
               println("permission true oldu")
           }
        }

    }

    private fun fetchLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            println("fetchte izin varmisa girdi")
            val location =  fusedLocationProviderClient.lastLocation
            location.addOnSuccessListener {userLocation ->
                if(userLocation != null){
                    homeActivityViewModel.giveLocationPermission()
                    println("istedi")
                    val lat = userLocation.latitude
                    val long = userLocation.longitude
                    auth.currentUser?.uid?.let { userId ->
                        db.collection("Users").document(userId)
                            .update("location", listOf(lat,long))
                    }
                }else {
                    //LOKASYON ALINAMADI
                }
            }
            return
        }else {
            println("fetchde izin yokmuşa girdi izin istedi")
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        }

    }


    private fun bottomBarReplaceFragments(fragment : Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerFragment, fragment)
            commit()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("override girdi")
        if (requestCode == 100) {
            println("request == 100 müş")
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                println("override fetch dedi")
                // İzin verildiyse konumu al
                fetchLocation()
                homeActivityViewModel.giveLocationPermission()
            } else {
                Snackbar.make(binding.root, "Ayarlardan konum izni vermelisin!.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ayarlara Git") {
                        //ayarlara yönlendir.
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                        shouldCheckPermissionOnResume = true
                    }.show()
                println("İzin reddedildi")
            }
        }
    }

}