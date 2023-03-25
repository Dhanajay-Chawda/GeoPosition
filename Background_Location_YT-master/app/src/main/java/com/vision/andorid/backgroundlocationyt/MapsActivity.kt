package com.vision.andorid.backgroundlocationyt

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*

import com.vision.andorid.backgroundlocationyt.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var location: Location? = null

    lateinit var databaseReference: DatabaseReference
    var previousLatLng: LatLng? = null
    var currentLatLng: LatLng? = null
    private var polyline1: Polyline? = null

    private val polylinePoints: ArrayList<LatLng> = ArrayList()
    private var mCurrLocationMarker: Marker? = null


    /* var databaseRead = FirebaseDatabase.getInstance("https://background-location-a38df-default-rtdb.asia-southeast1.firebasedatabase.app/")
    var myRef = databaseRead.getReference();*/

    //lateinit var lon:String
    // lateinit var lat:String

    // var myLatLng = LatLng(myMarker.getPosition().latitude, myMarker.getPosition().longitude)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)














        /*  val mapListner = object : ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {
                  lon  = snapshot.child("log").getValue().toString();

                  lat = snapshot.child("let").getValue().toString();

                  vari1=lon.toDouble()
                  vari2=lat.toDouble()
                  val latLng = LatLng(lon,lat);

                  Log.i("Maplistner", lon.toString()+" "+lat.toString());

              }

              override fun onCancelled(error: DatabaseError) {
                  TODO("Not yet implemented")
              }

          }
          myRef.addValueEventListener(mapListner)*/

      }



        override fun onMapReady(googleMap: GoogleMap) {


            mMap = googleMap


            /*  Log.i("MapLogger","location $vari1 $vari2");

       /*val latLng = LatLng(lon,lat);
        val markerOptions = MarkerOptions().position(latLng).title("I am here!")

        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        googleMap?.addMarker(markerOptions)*/

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
            setPolylines()
            fetchUpdatedLocation()
        }


   private fun setPolylines() {
        val polylineOptions = PolylineOptions()
        polylineOptions.color.blue
        polylineOptions.geodesic(true)

        polyline1 = mMap.addPolyline(polylineOptions.addAll(polylinePoints))
    }




    private  fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
            // below line is use to generate a drawable.
            val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

            // below line is use to set bounds to our vector drawable.
            vectorDrawable!!.setBounds(
                0,
                0,
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            )

            // below line is use to create a bitmap for our
            // drawable which we have added.
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )

            // below line is use to add bitmap in our canvas.
            val canvas = Canvas(bitmap)

            // below line is use to draw our
            // vector drawable in canvas.
            vectorDrawable.draw(canvas)

            // after generating our bitmap we are returning our bitmap.
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }




    private fun updateMap(dataSnapshot: DataSnapshot) {
        var latitude = 0.0
        var longitude = 0.0
        val data = dataSnapshot.childrenCount

        for (d in 0 until data) {
            latitude = dataSnapshot.child("let").getValue(Double::class.java)!!.toDouble()
            longitude =
                dataSnapshot.child("log").getValue(Double::class.java)!!.toDouble()

            Log.i("Dhananjay", "Value is: " + latitude + " "+longitude);
        }

        currentLatLng = LatLng(latitude, longitude)

        if (previousLatLng == null || previousLatLng !== currentLatLng) {
            // add marker line
            previousLatLng = currentLatLng
            polylinePoints.add(currentLatLng!!)
            polyline1!!.points = polylinePoints

            if (mCurrLocationMarker != null) {
                mCurrLocationMarker!!.position = currentLatLng!!
            } else {
                mCurrLocationMarker = mMap.addMarker(
                    MarkerOptions()
                        .position(currentLatLng!!)
                        .icon(bitmapFromVector(applicationContext, R.drawable.ic_baseline_location_on_24))
                )
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng!!, 16f))
        }
    }




    private fun fetchUpdatedLocation() {

        databaseReference = FirebaseDatabase.getInstance("https://background-location-a38df-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Location")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                updateMap(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    }





