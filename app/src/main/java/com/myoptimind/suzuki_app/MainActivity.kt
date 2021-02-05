package com.myoptimind.suzuki_app

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.myoptimind.suzuki_app.features.MainViewModel
import com.myoptimind.suzuki_app.features.login.LoginActivity
import com.myoptimind.suzuki_app.features.login.LoginViewModel
import com.myoptimind.suzuki_app.features.shared.AppSharedPref
import com.myoptimind.suzuki_app.features.shared.InfoDialogFragment
import com.myoptimind.suzuki_app.features.shared.MyFirebaseMessagingService
import com.myoptimind.suzuki_app.features.shared.api.Result
import com.myoptimind.suzuki_app.features.sidenav.changepass.ChangePasswordDialogFragment
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.ServiceHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.partial_nav_bottom.*
import kotlinx.android.synthetic.main.partial_nav_top.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import javax.inject.Inject





private const val REQUEST_IMAGE_CAPTURE = 1

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener, EasyPermissions.PermissionCallbacks {

    companion object {

        private const val RC_CAMERA = 9100;

        private const val URL_WEBSITE = "https://mc.suzuki.com.ph"
        private const val URL_FACEBOOK = "https://www.facebook.com/SuzukiMotorcyclesPhilippines"
        public const val URL_YOUTUBE = "https://www.youtube.com/channel/UCGj0SA4w71mC2o23EFrWzAg"
        private const val URL_INSTAGRAM = "https://www.instagram.com/suzukimotorsph/"
        private const val URL_TWITTER = "https://www.twitter.com"
        private const val URL_PRIVACY_POLICY = "https://mc.suzuki.com.ph/privacy-policy/"
        private const val URL_ABOUT = "https://www.suzuki.com.ph/company/"
    }

    lateinit var currentPhotoPath: String
    var searchListener: SearchListener? = null
    private val viewModel: MainViewModel by viewModels()
    private val serviceHistoryViewModel: ServiceHistoryViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var appSharedPref: AppSharedPref

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(requestCode == RC_CAMERA && perms.contains(android.Manifest.permission.CAMERA) && perms.contains(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            dispatchTakePictureIntent()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(requestCode == RC_CAMERA){
            Toast.makeText(this,"Permission denied.",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras.let {
            if(it != null && it.containsKey(MyFirebaseMessagingService.EXTRA_WHATS_NEW)){
                findNavController(R.id.nav_host_container).navigate(R.id.action_global_whatsNewFragment)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent.extras.let {
            if(it != null && it.containsKey(MyFirebaseMessagingService.EXTRA_WHATS_NEW)){
                findNavController(R.id.nav_host_container).navigate(R.id.action_global_whatsNewFragment)
            }
        }

        cb_notifications.isChecked = appSharedPref.isNotificationEnable
        cb_notifications.setOnCheckedChangeListener { _, isChecked ->
            appSharedPref.enableNotification(isChecked)
        }

        Glide.with(this)
                .load(R.drawable.loading)
                .into(view_loading)

        Glide.with(this)
                .load(R.drawable.loading)
                .into(view_loading_profile_picture)

        val user = appSharedPref.getUserFromPrefs()

        loadProfilePicture(user.profilePicture)



        serviceHistoryViewModel.getUpcomingEvents()
        viewModel.updateProfilePictureResult.observe(this){ result ->
            when(result){
                Result.Loading -> {
                    view_loading_profile_picture.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val response = result.data
                    Toast.makeText(this,response.meta.message,Toast.LENGTH_LONG).show()
                    view_loading_profile_picture.visibility = View.GONE
                    loadProfilePicture(result.data.data.profilePicture)
                    viewModel.resetResult()
                }
                is Result.Error -> {
                    Toast.makeText(this,"Something went wrong.",Toast.LENGTH_LONG).show()
                    viewModel.resetResult()
                    view_loading_profile_picture.visibility = View.GONE
                }
            }
        }

        serviceHistoryViewModel.upcomingEventsResult.observe(this){ result ->
            when(result){
                Result.Loading -> { }
                is Result.Success -> {
                    if(result.data.data.isNotEmpty()){
                        Glide.with(this)
                                .load(R.drawable.ic_notification_on)
                                .centerInside()
                                .into(ib_top_notification)
                    }else{
                        Glide.with(this)
                                .load(R.drawable.ic_notification_off)
                                .centerInside()
                                .into(ib_top_notification)
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this,result.error.message,Toast.LENGTH_LONG).show()
                }
            }
        }

        loginViewModel.logoutResult.observe(this){ result ->
            when(result){
                Result.Loading -> {
                    showLoading()
                }
                is Result.Success -> {
                    if(result.data.meta.status == "200"){
                        this.finish()
                        startActivity(
                                Intent(this, LoginActivity::class.java)
                        )
                        LoginManager.getInstance().logOut()
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .build()

                        GoogleSignIn.getClient(this, gso).signOut()
                        appSharedPref.removeUserId()
                    }
                    loginViewModel.clearLogoutResult()
                }
                is Result.Error -> {
                    Toast.makeText(this,"Something went wrong. Please Try Again.",Toast.LENGTH_LONG).show()
                    loginViewModel.clearLogoutResult()
                }
            }
        }



        ib_menu_motorcycle.setOnClickListener(this)
        ib_menu_servicecenters.setOnClickListener(this)
        ib_menu_dealerlocator.setOnClickListener(this)
        ib_menu_spareparts.setOnClickListener(this)
        ib_menu_my_suzuki_diary.setOnClickListener(this)
        tv_about_suzuki.setOnClickListener(this)
        tv_privacy_policy.setOnClickListener(this)
        tv_terms_and_conditions.setOnClickListener(this)
        fab_submenu.setOnClickListener(this)
        ib_nav_back.setOnClickListener(this)
        ib_search.setOnClickListener(this)
        ib_top_menu.setOnClickListener(this)
        tv_signout.setOnClickListener(this)
        ib_signout.setOnClickListener(this)
        label_change_password.setOnClickListener(this)
        tv_suzuki_website.setOnClickListener(this)
        ib_nav_fb.setOnClickListener(this)
        ib_nav_youtube.setOnClickListener(this)
        ib_nav_instagram.setOnClickListener(this)
//        ib_nav_twitter.setOnClickListener(this)
        profile_image.setOnClickListener(this)
        tv_contact_us.setOnClickListener(this)
        ib_top_notification.setOnClickListener(this)


        findNavController(R.id.nav_host_container).addOnDestinationChangedListener { _, destination, _ ->

            ib_menu_motorcycle.offIfActive(R.drawable.ic_motorcycles_on, R.drawable.ic_motorcycles_off)
            ib_menu_spareparts.offIfActive(R.drawable.ic_spare_parts_on, R.drawable.ic_spare_parts_off)
            ib_menu_servicecenters.offIfActive(R.drawable.ic_service_centers_on, R.drawable.ic_service_centers_off)
            ib_menu_dealerlocator.offIfActive(R.drawable.ic_dealer_locator_on, R.drawable.ic_dealer_locator_off)
            ib_menu_my_suzuki_diary.offIfActive(R.drawable.ic_suzuki_diary_on, R.drawable.ic_suzuki_diary_off)

            when (destination.id) {
                R.id.motorcycleModelsFragment -> ib_menu_motorcycle.setImageDrawable(getDrawable(R.drawable.ic_motorcycles_on))
                R.id.sparePartsFragment -> ib_menu_spareparts.setImageDrawable(getDrawable(R.drawable.ic_spare_parts_on))
                R.id.serviceCentersFragment -> ib_menu_servicecenters.setImageDrawable(getDrawable(R.drawable.ic_service_centers_on))
                R.id.dealerLocatorsFragment -> ib_menu_dealerlocator.setImageDrawable(getDrawable(R.drawable.ic_dealer_locator_on))
                R.id.fragmentSuzukiDiary -> ib_menu_my_suzuki_diary.setImageDrawable(getDrawable(R.drawable.ic_suzuki_diary_on))
            }

        }
        setSupportActionBar(toolbar)
    }

    private fun loadProfilePicture(imageUrl: String){
        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .into(profile_image)
    }

    override fun onClick(view: View?) {
        when (view) {

            // top nav
            ib_nav_back -> onBackPressed()
            ib_search -> searchListener?.onSearch(et_top_search.text.toString())
            ib_top_menu -> {
                if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
                    drawer_layout.closeDrawers()
                } else {
                    drawer_layout.openDrawer(GravityCompat.END)
                }
            }
            ib_top_notification -> findNavController(R.id.nav_host_container).navigate(R.id.action_global_upcomingServiceEventsFragment)


            // bottom nav
            fab_submenu -> findNavController(R.id.nav_host_container).navigate(R.id.action_global_fullMenuFragment)
            ib_menu_motorcycle -> Navigation.findNavController(this, R.id.nav_host_container).navigate(R.id.action_global_motorcycleModelsFragment)
            ib_menu_dealerlocator -> Navigation.findNavController(this, R.id.nav_host_container).navigate(R.id.action_global_dealerLocatorsFragment)
            ib_menu_servicecenters -> Navigation.findNavController(this, R.id.nav_host_container).navigate(R.id.action_global_serviceCentersFragment)
            ib_menu_spareparts -> {
                //todo: enable this on next update
                InfoDialogFragment.newInstance("Sorry!","Contents for spare parts are coming soon.").show(supportFragmentManager,"spare_parts_disable")
//                Navigation.findNavController(this, R.id.nav_host_container).navigate(R.id.action_global_sparePartsFragment)
            }
            ib_menu_my_suzuki_diary -> Navigation.findNavController(this, R.id.nav_host_container).navigate(R.id.action_global_suzukiDiary)


            //side nav
            label_change_password -> {
                ChangePasswordDialogFragment().show(supportFragmentManager, "change_pass")
            }
            tv_contact_us -> {
                hideSideNav()
                Navigation.findNavController(this,R.id.nav_host_container).navigate(R.id.action_global_customerCareFragment)
            }
            tv_about_suzuki -> {
                hideSideNav()
//                Navigation.findNavController(this, R.id.nav_host_container).navigate(R.id.action_global_aboutSuzukiFragment)
                openWebPage(URL_ABOUT)
            }
            tv_terms_and_conditions -> {
                hideSideNav()
                openWebPage(URL_PRIVACY_POLICY)

//                Navigation.findNavController(this, R.id.nav_host_container).navigate(R.id.action_global_termsAndConditionsFragment)
            }
            tv_privacy_policy -> {
                openWebPage(URL_PRIVACY_POLICY)
               /* hideSideNav()
                Navigation.findNavController(this, R.id.nav_host_container).navigate(R.id.action_global_privacyPolicyFragment)*/
            }
            tv_suzuki_website -> openWebPage(URL_WEBSITE)
            ib_nav_fb -> openWebPage(URL_FACEBOOK)
            ib_nav_youtube -> openWebPage(URL_YOUTUBE)
            ib_nav_instagram -> openWebPage(URL_INSTAGRAM)
//            ib_nav_twitter -> openWebPage(URL_TWITTER)
            tv_signout,ib_signout -> { // logout
                loginViewModel.logout()
            }
            profile_image -> {
                val permissions = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if(EasyPermissions.hasPermissions(this,*permissions)){
                    dispatchTakePictureIntent()
                }else{
                    EasyPermissions.requestPermissions(
                            PermissionRequest.Builder(this, RC_CAMERA, android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .build()
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    fun hideSideNav() {
        if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
            drawer_layout.closeDrawers()
        }
    }


    internal fun hideBottomNav(hide: Boolean) {
        if (hide) {
            group_bottom_nav.hide()
            fab_submenu.hide()
        } else {
            group_bottom_nav.show()
            fab_submenu.show()
        }
    }

    internal fun showSearch() {
        iv_nav_logo.hide()
        ib_top_menu.hide()
        nav_top_title.hide()

        group_search.show()
        ib_nav_back.show()
    }

    internal fun showLogoOnly() {

        ib_nav_back.hide()
        group_search.hide()
        nav_top_title.hide()

        iv_nav_logo.show()
        ib_top_menu.show()
    }

    internal fun showTitleOnly() {
        group_search.hide()
        iv_nav_logo.hide()
        ib_top_menu.hide()

        ib_nav_back.show()
        nav_top_title.show()
    }

    internal fun showLoading() {
        if (!view_loading.isVisible) {
            view_loading.visibility = View.VISIBLE
        }
    }

    internal fun hideLoading() {
        if (view_loading.isVisible) {
            view_loading.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val uploadedPhoto = File(currentPhotoPath)
            viewModel.updateProfilePicture(uploadedPhoto)
            Timber.v("success upload")
        }
    }

    interface SearchListener {
        fun onSearch(keyword: String)
    }

    private fun View.show() {
        this.visibility = View.VISIBLE
    }

    private fun View.hide() {
        this.visibility = View.GONE
    }


    private fun ImageView.offIfActive(
            activeStateDrawableId: Int,
            inactiveStateDrawableId: Int
    ) {
        if (this.drawable.constantState == getDrawable(activeStateDrawableId)?.constantState) {
            this.setImageDrawable(getDrawable(inactiveStateDrawableId))
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(this.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.myoptimind.suzuki_app.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent,
                            REQUEST_IMAGE_CAPTURE
                    )
                }
            }
        }
    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = timeStamp + ".jpg"
        val storageDir = File(this.filesDir, "uploaded")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }else{
            // clear images if there's any
            storageDir.listFiles {
                file -> file.delete()
            }
        }
        val image = File(
                storageDir,
                imageFileName
        )
        currentPhotoPath = image.absolutePath
        return image
    }
}

