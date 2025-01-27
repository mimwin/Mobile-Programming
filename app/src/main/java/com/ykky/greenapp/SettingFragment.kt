package com.ykky.greenapp

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ykky.greenapp.databinding.FragmentSettingBinding
import com.ykky.greenapp.databinding.MypageditBinding
import java.io.ByteArrayOutputStream
import java.io.InputStream


class SettingFragment : Fragment() {

    lateinit var re1:LinearLayout
    lateinit var re2:GridLayout
    lateinit var re3:LinearLayout
    lateinit var firebaseauth : FirebaseAuth
    lateinit var databaseref : DatabaseReference
    lateinit var firebaseUser:FirebaseUser
    var binding:FragmentSettingBinding?=null

    var emailId=""
    var password=""
    var nickname=""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMyPage()
        init()

    }

    private fun initMyPage() {
        firebaseauth= FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")

        firebaseUser = firebaseauth.currentUser!!

        databaseref.child("UserAccount").child(firebaseUser?.uid.toString())
            .get().addOnSuccessListener {
                emailId=it.child("emailId").value.toString()
                password=it.child("password").value.toString()
                nickname=it.child("nickname").value.toString()


                binding?.apply {
                    myid.text="아이디 : $emailId"
                    mypassword.text="비밀번호 : $password"
                    mynickname.text="닉네임 : $nickname"
                }
        }


        binding!!.apply{
            mypageeidt.setOnClickListener {
                val data=arrayOf(emailId, password, nickname)
                AlertEdit(data)
            }
            mypagedelete.setOnClickListener {
                AlertDelete()
            }
            mypagelogout.setOnClickListener {
                //로그인 화면으로 돌아가기
                val intent= Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    //데이터 수정 알림창
    private fun AlertEdit(data: Array<String>) {
        val dlgBinding= MypageditBinding.inflate(layoutInflater)
        dlgBinding.id.text=data[0]
        dlgBinding.editpassword.setText(data[1])
        dlgBinding.editnickaname.setText(data[2])


        val builder= AlertDialog.Builder(requireContext())
        builder.setView(dlgBinding.root)
            .setTitle("마이 페이지 수정하기")
            .setPositiveButton("네"){ _, _ ->
                val newpassword=dlgBinding.editpassword.text.toString()
                data[1]=newpassword
                val newnickname=dlgBinding.editnickaname.text.toString()
                data[2]=newnickname
                //데이터베이스에 저장
                databaseref.child("UserAccount").child(firebaseUser?.uid.toString())
                    .child("password").setValue(newpassword)
                databaseref.child("UserAccount").child(firebaseUser?.uid.toString())
                    .child("nickname").setValue(newnickname)

                Toast.makeText(requireContext(), "마이 페이지 수정 완료", Toast.LENGTH_SHORT).show()

                //마이 페이지에 변경된 부분 반영
                binding!!.mypassword.text="비밀번호 : ${data[1]}"
                password=data[1]
                binding!!.mynickname.text="닉네임 : ${data[2]}"
                nickname=data[2]

            }
            .setNegativeButton("아니오"){ _, _ ->
            }
        val dlg=builder.create()
        dlg.show()
    }

    //계정 삭제 알림창
    private fun AlertDelete() {
        val builder= AlertDialog.Builder(requireContext())
        builder.setMessage("해당 계정을 삭제하시겠습니까? \n계정 삭제시 모든 TODO가 사라집니다.")
            .setPositiveButton("네"){ _, _ ->
                //데이터베이스에서 삭제하기
                databaseref.child("UserAccount").child(firebaseUser?.uid.toString()).removeValue()
                databaseref.child("Leaderboard").child(firebaseUser?.uid.toString()).removeValue()
                firebaseUser.delete()

                Toast.makeText(requireContext(), "계정 삭제 완료", Toast.LENGTH_SHORT).show()

                //로그인 화면으로 돌아가기
                val intent= Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("아니오"){ _, _ ->
            }
        val dlg=builder.create()
        dlg.show()
    }

    private fun init() {
        binding!!.apply{
            //imagview clicklistener
            backimg1.setOnClickListener {
                databaseref.child("UserAccount").child(firebaseUser.uid.toString())
                    .child("isDrawable").setValue(true)
                databaseref.child("UserAccount").child(firebaseUser.uid.toString())
                    .child("Image").setValue(0)
                (activity as MainActivity).replaceFragment(GreenFragment(), "navgreen")
            }
            backimg2.setOnClickListener {
//                (activity as MainActivity).setBackImg(1)
                databaseref.child("UserAccount").child(firebaseUser.uid.toString()).child("isDrawable").setValue(true)
                databaseref.child("UserAccount").child(firebaseUser.uid.toString()).child("Image").setValue(1)
                (activity as MainActivity).replaceFragment(GreenFragment(), "navgreen")
            }
            backimg3.setOnClickListener {
//                (activity as MainActivity).setBackImg(2)
                databaseref.child("UserAccount").child(firebaseUser.uid.toString()).child("isDrawable").setValue(true)
                databaseref.child("UserAccount").child(firebaseUser.uid.toString()).child("Image").setValue(2)
                (activity as MainActivity).replaceFragment(GreenFragment(), "navgreen")
            }

            mypagedown.setOnClickListener {
                re1=binding!!.maypagedata
                if(re1.visibility==View.VISIBLE){
                    re1.visibility=View.GONE
                }
                else if(re1.visibility==View.GONE){
                    re1.visibility=View.VISIBLE
                }
            }
            changebackgrounddown.setOnClickListener {
                re2 = binding!!.changeback
                if(re2.visibility==View.VISIBLE){
                    re2.visibility=View.GONE
                }
                else if(re2.visibility==View.GONE){
                    re2.visibility=View.VISIBLE
                }
            }
            devinfodown.setOnClickListener {
                re3=binding!!.devinfodata
                if(re3.visibility==View.VISIBLE){
                    re3.visibility=View.GONE
                }
                else if(re3.visibility==View.GONE){
                    re3.visibility=View.VISIBLE
                }
            }

            plusbtn.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 100)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
    lateinit var uri : Uri

    fun BitmapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos)
        val bytes: ByteArray = baos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                try {
                    uri = data?.data!!
                    val `in`: InputStream = activity?.contentResolver?.openInputStream(uri)!!
                    val img = BitmapFactory.decodeStream(`in`)
//                    val s = BitmapToString(img)!!
//                    (activity as MainActivity).setBackImg(s)

//                    `in`.close()
                    databaseref.child("UserAccount").child(firebaseUser.uid.toString()).child("isDrawable").setValue(false)
                    val path = getPathFromUri(requireContext(), uri)
                    Log.e("Settingpath", path)
                    databaseref.child("UserAccount").child(firebaseUser.uid.toString()).child("Image").setValue(path)
                    (activity as MainActivity).replaceFragment(GreenFragment(), "navgreen")
                } catch (e: Exception) {
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(context, "사진 선택 취소", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getPathFromUri(context: Context, uri: Uri): String {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            when (uri.authority) {
                "com.android.externalstorage.documents" -> {
                    val documentId: String = DocumentsContract.getDocumentId(uri)
                    val documentIdSplited: List<String> =
                            documentId.split(":") // left is storage identifier, right is path
                    if (documentIdSplited[0].toLowerCase() == "primary") {
                        return "${Environment.getExternalStorageDirectory()}/${documentIdSplited[1]}"
                    }
                }

                "com.android.providers.downloads.documents" -> {
                    val documentId: String = DocumentsContract.getDocumentId(uri)

                    // This is RAW Path
                    if (documentId.startsWith("raw:")) {
                        return documentId.replaceFirst("raw:", "")
                    }

                    // OR else?
                    val documentLongId: Long = try {
                        java.lang.Long.valueOf(documentId)
                    } catch (e: NumberFormatException) {
                        return "ERROR"
                    }
                    val contentUri: Uri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            documentLongId,
                    )
                    return accessContentProviderDb(context, contentUri, null, null)
                            ?: "ERROR"
                }
            }
        }
        return ""
    }

    fun accessContentProviderDb(
            context: Context,
            queryUri: Uri,
            selectionQuery: String?,
            querySelectionArgs: Array<String>?
    ): String? {
        // The query column:[android.providers.Downloads.impl._DATA] --> Owner Can Read + Containing file name
        // https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/provider/Downloads.java
        // "Absolute filesystem path to the media item on disk."
        val queryFindColumn: String = "_data"
        val projectionList: Array<String> = arrayOf(queryFindColumn)
        var queryCursor: Cursor = context.contentResolver.query(
                queryUri, projectionList, selectionQuery, querySelectionArgs, null
        ) ?: run {
            Log.e("FLOWER", "Query Result[Cursor] is responded with null!")
            return null
        }

        // Set table cursor to first[Since URI Query - exact search result should be first though]
        return if (!queryCursor.moveToFirst()) {
            Log.e("FLOWER", "Cannot move table cursor to first row!")
            null
        } else {
            val tableIndex: Int = queryCursor.getColumnIndex(queryFindColumn)
            queryCursor.getString(tableIndex)
        }
    }
}



