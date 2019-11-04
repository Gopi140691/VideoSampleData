package com.example.gopinathal.videoplayer

import android.Manifest
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.*
import android.opengl.ETC1.getHeight
import android.support.constraint.ConstraintLayout
import android.view.animation.TranslateAnimation




class DashBoardActivity : AppCompatActivity(),View.OnClickListener{
    override fun onClick(p0: View) {
        when(p0.id){
            R.id.video_container->{
                rvList.visibility = View.VISIBLE
                Toast.makeText(this,"click",Toast.LENGTH_LONG).show()
            }
        }


    }


    lateinit var vw: VideoView
    lateinit var videoItemHash: HashSet<String>
    var projection = arrayOf<String>()

    lateinit var cursor: Cursor

    lateinit var arrlist: ArrayList<String>

    lateinit var rvList: RecyclerView

    lateinit var videolistadapter: VideoListAdapter

    private lateinit var managePermissions: ManagePermissions

    private val PermissionsRequestCode = 123

    lateinit var  mediaController: MediaController

    lateinit var uri :Uri

    lateinit var container:ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        vw = findViewById(R.id.video_container) as VideoView
        rvList = findViewById(R.id.recyclerView) as RecyclerView
        container = findViewById(R.id.container) as ConstraintLayout

        rvList.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)

        rvList.visibility = View.INVISIBLE

        arrlist = ArrayList<String>()
        mediaController = MediaController(this)
        val list = listOf<String>(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this, list, PermissionsRequestCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()

        vw.setOnClickListener(this)
    }

    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                        .processPermissionsResult(requestCode, permissions, grantResults)

                if (isPermissionsGranted) {
                    // Do the task now
                   // toast("Permissions granted.")
                    getAllMedia()
                } else {
                    //toast("Permissions denied.")
                }
                return
            }
        }
    }
    private fun getAllMedia(): ArrayList<String> {

        videoItemHash = HashSet<String>()
        projection = arrayOf(MediaStore.Video.VideoColumns.DATA ,MediaStore.Video.Media.DISPLAY_NAME)
        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)

        try {
            cursor.moveToFirst();
            do{
                videoItemHash.add((cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))));
            }while(cursor.moveToNext());
            cursor.close();
        }catch (e:Exception) {
            e.printStackTrace();
        }

        arrlist = ArrayList<String>(videoItemHash)
        videolistadapter = VideoListAdapter(arrlist)
        rvList.adapter = videolistadapter

        mediaController.setAnchorView(vw)

        uri= Uri.parse(arrlist.get(0))

        vw.setMediaController(mediaController);
        vw.setVideoURI(uri);
        vw.requestFocus();
        vw.start();

        Log.e("sizeee1",""+arrlist.size)
        Log.e("sizeee2",""+arrlist.toString())
        return arrlist

    }
}



