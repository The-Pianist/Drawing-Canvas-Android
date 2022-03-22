package com.example.drawingapp2

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.brushsize.*
import kotlinx.android.synthetic.main.color_buttons.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView.setSizeForBursh(20.toFloat())

        brushbtn.setOnClickListener { showBrushSizeChooserDialog() }

        colorbtn.setOnClickListener { setColor() }

        val loadingImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback { bgimage.setImageURI(it) })
        btnbg.setOnClickListener {
            if (checkP()) {
                loadingImage.launch("image/*")

            } else {
                requestP()
            }
        }

        btnremove.setOnClickListener {
            drawingView.clear()
        }


        btnsaving.setOnClickListener {
            if (checkP()) {
                    sending(getgrapthic(bglayout))
            } else {
                requestP()
            }
            Toast.makeText(this,"download success",Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Grant permission for bg", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val permissionCode = 2
    }

    private fun requestP() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).toString()
            )
        ) {
            Toast.makeText(this, "Grant Permission for bg", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), permissionCode
            )
        }
    }

    private fun checkP(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun showBrushSizeChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.brushsize)
        brushDialog.setTitle("Brush size :")
        val smallBtn = brushDialog.smallbrush
        smallBtn.setOnClickListener(View.OnClickListener {
            drawingView.setSizeForBursh(10.toFloat())
            brushDialog.dismiss()
        })
        val mediumBtn = brushDialog.mediumbrush
        mediumBtn.setOnClickListener(View.OnClickListener {
            drawingView.setSizeForBursh(20.toFloat())
            brushDialog.dismiss()
        })

        val largeBtn = brushDialog.largebrush
        largeBtn.setOnClickListener(View.OnClickListener {
            drawingView.setSizeForBursh(30.toFloat())
            brushDialog.dismiss()
        })
        brushDialog.show()
    }

    private fun setColor() {
        val colordialog = Dialog(this)
        colordialog.setContentView(R.layout.color_buttons)
        colordialog.setTitle("Choose color")
        val btn1 = colordialog.blue
        btn1.setOnClickListener(View.OnClickListener {
            drawingView.setcolor(Color.BLUE)
            colordialog.dismiss()
        })
        val btn2 = colordialog.gray
        btn2.setOnClickListener(View.OnClickListener {
            drawingView.setcolor(Color.GRAY)
            colordialog.dismiss()
        })
        val btn3 = colordialog.cyan
        btn3.setOnClickListener(View.OnClickListener {
            drawingView.setcolor(Color.CYAN)
            colordialog.dismiss()
        })
        val btn4 = colordialog.green
        btn4.setOnClickListener(View.OnClickListener {
            drawingView.setcolor(Color.GREEN)
            colordialog.dismiss()
        })
        val btn5 = colordialog.red
        btn5.setOnClickListener(View.OnClickListener {
            drawingView.setcolor(Color.RED)
            colordialog.dismiss()
        })
        val btn6 = colordialog.yellow
        btn6.setOnClickListener(View.OnClickListener {
            drawingView.setcolor(Color.YELLOW)
            colordialog.dismiss()
        })
        val btn7 = colordialog.black
        btn7.setOnClickListener(View.OnClickListener {
            drawingView.setcolor(Color.BLACK)
            colordialog.dismiss()
        })
        colordialog.show()
    }

    private fun getgrapthic(view: View): Bitmap {

        val returnbit: Bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnbit)
        val bgdrawable = view.background
        if (bgdrawable != null) {
            bgdrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnbit
    }

    private fun sending(mbitmap: Bitmap?) :String{
        var result = ""
        if (mbitmap != null) {
            try {
                val bytes=ByteArrayOutputStream()
                mbitmap.compress(Bitmap.CompressFormat.PNG,90,bytes)
                val f=File(
                    externalCacheDir!!.absoluteFile.toString()+
       File.separator+"drawingapp2"+System.currentTimeMillis()/1000+".jpg")
                val fo=FileOutputStream(f)
                fo.write(bytes.toByteArray())
                fo.close()
                result=f.absolutePath
            }catch (e:Exception){
                Toast.makeText(this,"something goes wrong",Toast.LENGTH_LONG).show() } }
        return result
            }



}

