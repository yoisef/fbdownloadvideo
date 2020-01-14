package videodownloader.FBVideos.adapter;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import videodownloader.FBVideos.PrefManager;
import videodownloader.FBVideos.R;
import videodownloader.FBVideos.VideoPlayerActivity;
import videodownloader.FBVideos.fbbrowse;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by Adi on 25-03-2017.
 */
public class HomeFragment extends Fragment  {
    WebView mWebview ;
    private ProgressBar mprogress;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private int isInternetConnected=1;
    public InterstitialAd mInterstitialAd;
    View mainView;
    private static final String ARG_POSITION = "position";
    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private PrefManager pref;
    public static ArrayList<String> downloadlist=new ArrayList<String>();
    public static ArrayList<String> profilepublicList=new ArrayList<>();
    public static HomeFragment newInstance(int position) {
        HomeFragment f = new HomeFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    EditText searchbar;
    ImageButton gosearch;
    String urlvideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loadinterstial();
        interstiallistener();

        fbbrowse activity=(fbbrowse)getActivity();
       urlvideo= activity.geturlvalue();


        mainView=inflater.inflate(R.layout.fragment_home,container,false);
        setRetainInstance(true);
        mySwipeRefreshLayout = (SwipeRefreshLayout)mainView.findViewById(R.id.swipeContainer);
        searchbar=mainView.findViewById(R.id.searchedit);
        gosearch=mainView.findViewById(R.id.gosearchbut);
        pref = new PrefManager(getContext());
        mprogress= (ProgressBar) mainView.findViewById(R.id.progressBar);
        mprogress.setProgress(0);
        mprogress.setMax(100);
        mWebview  = (WebView) mainView.findViewById(R.id.webView);
        mWebview.setVisibility(View.INVISIBLE);
        mWebview.getSettings().setSupportZoom(true);       //Zoom Control on web (You don't need this
        mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.addJavascriptInterface(this, "mJava");
        mWebview.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            mWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            if (mprogress.getProgress()==100) {
                                mprogress.setVisibility(View.INVISIBLE);
                                mWebview.setVisibility(View.VISIBLE);
                                mySwipeRefreshLayout.setRefreshing(false);
                                String url=  mWebview.getUrl();
                                searchbar.setText(url);
                                //  scrollview.scrollTo(0,0);
                            }
                        mWebview.loadUrl("javascript:"+
                                "var e=0;\n" +
                                "window.onscroll=function()\n" +
                                "{\n" +
                                "\tvar ij=document.querySelectorAll(\"video\");\n" +
                                "\t\tfor(var f=0;f<ij.length;f++)\n" +
                                "\t\t{\n" +
                                "\t\t\tif((ij[f].parentNode.querySelectorAll(\"img\")).length==0)\n" +
                                "\t\t\t{\n" +
                                "\t\t\t\tvar nextimageWidth=ij[f].nextSibling.style.width;\n" +
                                "\t\t\t\tvar nextImageHeight=ij[f].nextSibling.style.height;\n" +
                                "\t\t\t\tvar Nxtimgwd=parseInt(nextimageWidth, 10);\n" +
                                "\t\t\t\tvar Nxtimghght=parseInt(nextImageHeight, 10); \n" +
                                "\t\t\t\tvar DOM_img = document.createElement(\"img\");\n" +
                                "\t\t\t\t\tDOM_img.height=\"68\";\n" +
                                "\t\t\t\t\tDOM_img.width=\"68\";\n" +
                                "\t\t\t\t\tDOM_img.style.top=(Nxtimghght/2-20)+\"px\";\n" +
                                "\t\t\t\t\tDOM_img.style.left=(Nxtimgwd/2-20)+\"px\";\n" +
                                "\t\t\t\t\tDOM_img.style.position=\"absolute\";\n" +
                                "\t\t\t\t\tDOM_img.src = \"https://image.ibb.co/kobwsk/one.png\"; \n" +
                                "\t\t\t\t\tij[f].parentNode.appendChild(DOM_img);\n" +
                                "\t\t\t}\t\t\n" +
                                "\t\t\tij[f].remove();\n" +
                                "\t\t} \n" +
                                "\t\t\te++;\n" +
                                "};"+
                                "var a = document.querySelectorAll(\"a[href *= 'video_redirect']\");\n" +
                                "for (var i = 0; i < a.length; i++) {\n" +
                                "    var mainUrl = a[i].getAttribute(\"href\");\n" +
                                "  a[i].removeAttribute(\"href\");\n"+
                                "\tmainUrl=mainUrl.split(\"/video_redirect/?src=\")[1];\n" +
                                "\tmainUrl=mainUrl.split(\"&source\")[0];\n" +
                                "    var threeparent = a[i].parentNode.parentNode.parentNode;\n" +
                                "    threeparent.setAttribute(\"src\", mainUrl);\n" +
                                "    threeparent.onclick = function() {\n" +
                                "        var mainUrl1 = this.getAttribute(\"src\");\n" +
                                "         mJava.getData(mainUrl1);\n" +
                                "    };\n" +
                                "}"+
                                "var k = document.querySelectorAll(\"div[data-store]\");\n" +
                                "for (var j = 0; j < k.length; j++) {\n" +
                                "    var h = k[j].getAttribute(\"data-store\");\n" +
                                "    var g = JSON.parse(h);\nvar jp=k[j].getAttribute(\"data-sigil\");\n"+
                                "    if (g.type === \"video\") {\n" +
                                "if(jp==\"inlineVideo\")" +
                                "{" +
                                "   k[j].removeAttribute(\"data-sigil\");" +
                                "}\n" +
                                "        var url = g.src;\n" +
                                "        k[j].setAttribute(\"src\", g.src);\n" +
                                "        k[j].onclick = function() {\n" +
                                "            var mainUrl = this.getAttribute(\"src\");\n" +
                                "               mJava.getData(mainUrl);\n" +
                                "        };\n" +
                                "    }\n" +
                                "\n" +
                                "}");
                    }
                }, 3000);
            }

            public void onLoadResource(WebView view, String url) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebview.loadUrl("javascript:"+
                                "var e=document.querySelectorAll(\"span\"); " +
                                "if(e[0]!=undefined)" +
                                "{"+
                                "var fbforandroid=e[0].innerText;" +
                                "if(fbforandroid.indexOf(\"Facebook\")!=-1)" +
                                "{ " +
                                "var h =e[0].parentNode.parentNode.parentNode.style.display=\"none\";" +
                                "} " +
                                "}" +
                                "var installfb=document.querySelectorAll(\"a\");\n" +
                                "for (var hardwares = 0; hardwares < installfb.length; hardwares++) \n" +
                                "{\n" +
                                "\tif(installfb[hardwares].text.indexOf(\"Install\")!=-1)\n" +
                                "\t{\n" +
                                "\t\tvar soft=installfb[hardwares].parentNode.style.display=\"none\";\n" +
                                "\n" +
                                "\t}\n" +
                                "}\n");
                        mWebview.loadUrl("javascript:"+
                                "var e=0;\n" +
                                "window.onscroll=function()\n" +
                                "{\n" +
                                "\tvar ij=document.querySelectorAll(\"video\");\n" +
                                "\t\tfor(var f=0;f<ij.length;f++)\n" +
                                "\t\t{\n" +
                                "\t\t\tif((ij[f].parentNode.querySelectorAll(\"img\")).length==0)\n" +
                                "\t\t\t{\n" +
                                "\t\t\t\tvar nextimageWidth=ij[f].nextSibling.style.width;\n" +
                                "\t\t\t\tvar nextImageHeight=ij[f].nextSibling.style.height;\n" +
                                "\t\t\t\tvar Nxtimgwd=parseInt(nextimageWidth, 10);\n" +
                                "\t\t\t\tvar Nxtimghght=parseInt(nextImageHeight, 10); \n" +
                                "\t\t\t\tvar DOM_img = document.createElement(\"img\");\n" +
                                "\t\t\t\t\tDOM_img.height=\"68\";\n" +
                                "\t\t\t\t\tDOM_img.width=\"68\";\n" +
                                "\t\t\t\t\tDOM_img.style.top=(Nxtimghght/2-20)+\"px\";\n" +
                                "\t\t\t\t\tDOM_img.style.left=(Nxtimgwd/2-20)+\"px\";\n" +
                                "\t\t\t\t\tDOM_img.style.position=\"absolute\";\n" +
                                "\t\t\t\t\tDOM_img.src = \"https://image.ibb.co/kobwsk/one.png\"; \n" +
                                "\t\t\t\t\tij[f].parentNode.appendChild(DOM_img);\n" +
                                "\t\t\t}\t\t\n" +
                                "\t\t\tij[f].remove();\n" +
                                "\t\t} \n" +
                                "\t\t\te++;\n" +
                                "};"+
                                "var a = document.querySelectorAll(\"a[href *= 'video_redirect']\");\n" +
                                "for (var i = 0; i < a.length; i++) {\n" +
                                "    var mainUrl = a[i].getAttribute(\"href\");\n" +
                                "  a[i].removeAttribute(\"href\");\n"+
                                "\tmainUrl=mainUrl.split(\"/video_redirect/?src=\")[1];\n" +
                                "\tmainUrl=mainUrl.split(\"&source\")[0];\n"+
                                "    var threeparent = a[i].parentNode.parentNode.parentNode;\n" +
                                "    threeparent.setAttribute(\"src\", mainUrl);\n" +
                                "    threeparent.onclick = function() {\n" +
                                "        var mainUrl1 = this.getAttribute(\"src\");\n" +
                                "         mJava.getData(mainUrl1);\n" +
                                "    };\n" +
                                "}"+
                                "var k = document.querySelectorAll(\"div[data-store]\");\n" +
                                "for (var j = 0; j < k.length; j++) {\n" +
                                "    var h = k[j].getAttribute(\"data-store\");\n" +
                                "    var g = JSON.parse(h);var jp=k[j].getAttribute(\"data-sigil\");\n"+
                                "    if (g.type === \"video\") {\n" +
                                "if(jp==\"inlineVideo\")" +
                                "{" +
                                "   k[j].removeAttribute(\"data-sigil\");" +
                                "}\n" +
                                "        var url = g.src;\n" +
                                "        k[j].setAttribute(\"src\", g.src);\n" +
                                "        k[j].onclick = function() {\n" +
                                "            var mainUrl = this.getAttribute(\"src\");\n" +
                                "               mJava.getData(mainUrl);\n" +
                                "        };\n" +
                                "    }\n" +
                                "\n" +
                                "}");
                    }
                }, 3000);
            }
        });
        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                if (mprogress.getProgress()<100) {
                    String currentUrl=mWebview.getUrl();
                    if(currentUrl!=null) {
                        if (currentUrl.contains("/stories.php?aftercursorr")) {
                            mWebview.setVisibility(View.INVISIBLE);
                            mprogress.setVisibility(View.VISIBLE);
                            mWebview.scrollTo(0, 0);
                        }
                    }
                }
                mprogress.setProgress(progress);
            //   getActivity().setTitle("Loading...");
                getActivity().setProgress(progress * 100);
                //Make the bar disappear after URL is loaded
                // Return the app name after finish loading
                if(progress == 100)
                {
                    getActivity().setTitle(R.string.app_name);
                }
            }
        });
        if(isNetworkAvailable()) {
            isInternetConnected=1;

            if (urlvideo==null)
            {
                mWebview.loadUrl("https://m.facebook.com/");

                gosearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String weburl=searchbar.getText().toString();
                        mWebview.loadUrl(weburl);
                    }
                });
            }
            else{
                searchbar.setText(urlvideo);
                mWebview.loadUrl(urlvideo);

                gosearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String weburl=searchbar.getText().toString();
                        mWebview.loadUrl(weburl);
                    }
                });
            }

        }
        else
        {
            isInternetConnected=0;
            Toast.makeText(getContext(),"Please Connect to Internet",Toast.LENGTH_LONG).show();
        }
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebview.reload();
            }
        });
        mWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP){
                    if (mWebview.canGoBack()) {
                        mWebview.goBack();
                    } else {
                        getActivity().finish();
                    }
                }
                return true;
            }
        });
        return mainView;
    }
    public  void  loadinterstial()
    {
        MobileAds.initialize(getActivity(),
                "ca-app-pub-9508195472439107~6822869935");

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-9508195472439107/7641127585");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }

    public void interstiallistener()
    {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.

                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.

                mInterstitialAd.loadAd(new AdRequest.Builder().build());

            }
        });

    }
    public void getUrlfromUrlDownload(String url)
    {
        mWebview.loadUrl(url);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        permissionStatus = getActivity().getSharedPreferences("permissionStatus",getActivity().MODE_PRIVATE);
        if(isInternetConnected==0)
        {
            Snackbar.make(getView(),"Please Connect to Internet",Snackbar.LENGTH_LONG).show();
        }
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs phone permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getActivity(), "Go to Permissions to Grant Phone", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            if(allgranted){
                //  Toast.makeText(getActivity(),"Permissions Granted",Toast.LENGTH_LONG).show();
                mWebview.loadUrl("https://m.facebook.com/");
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs phone permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getActivity(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                Toast.makeText(getActivity(),"Permissions Granted",Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                Toast.makeText(getActivity(),"Permissions Granted",Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onPause() {

        if (mWebview.isShown() == false) {
            mWebview.stopLoading();
        }
        super.onPause();
    }
    @JavascriptInterface
    public void getData(final String pathvideo) {
        Log.d("scroled","jo");
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getResources().getString(R.string.savevideo));
        alertDialog.setMessage(getResources().getString(R.string.askdownload));
        alertDialog.setPositiveButton(getResources().getString(R.string.dialog_action_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String finalurl ;
                finalurl=pathvideo;
                finalurl=finalurl.replaceAll("%3A",":");
                finalurl=finalurl.replaceAll("%2F","/");
                finalurl=finalurl.replaceAll("%3F","?");
                finalurl=finalurl.replaceAll("%3D","=");
                finalurl=finalurl.replaceAll("%26","&");


                View view=LayoutInflater.from(getActivity()).inflate(R.layout.choosequailty,null);
                final Button download,cancel;
                final RadioGroup radiogroupp;
                final RadioButton radioButton;
                radiogroupp=view.findViewById(R.id.radiogroup);
                radiogroupp.clearCheck();
                download=view.findViewById(R.id.downloadvideo);
                cancel=view.findViewById(R.id.cancelvideo);
                final android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getActivity());
                builder

                        .setView(view);


                final android.app.AlertDialog alertDialog=builder.create();
                alertDialog.show();




                final String finalUrl = finalurl;
                download.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override public void run() {
                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                } else {
                                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                                }
                            }
                        });

                        // When submit button is clicked,
                        // Ge the Radio Button which is set
                        // If no Radio Button is set, -1 will be returned
                        int selectedId = radiogroupp.getCheckedRadioButtonId();
                        switch (selectedId)
                        {
                            case -1:{
                                Toast.makeText(getActivity(),"Choose Quailty",Toast.LENGTH_LONG).show();

                                break;
                            }
                            case R.id.hdqu:{
                                downloadvideo(finalUrl,0);
                                alertDialog.cancel();
                                break;
                            }
                            default:{
                                RadioButton radioButton
                                        = (RadioButton)radiogroupp
                                        .findViewById(selectedId);

                                // Now display the value of selected item
                                // by the Toast message
                                downloadvideo(finalUrl,1);

                                alertDialog.cancel();
                                break;
                            }
                        }

                    }
                });

                // Add the Listener to the Submit Button
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v)
                    {

                        alertDialog.cancel();
                        // Clear RadioGroup
                        // i.e. reset all the Radio Buttons
                        radiogroupp.clearCheck();
                    }
                });

            }


        });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(getResources().getString(R.string.watch), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(isNetworkAvailable()) {
                    String finalurl;
                    finalurl = pathvideo;
                    finalurl = finalurl.replaceAll("%3A", ":");
                    finalurl = finalurl.replaceAll("%2F", "/");
                    finalurl = finalurl.replaceAll("%3F", "?");
                    finalurl = finalurl.replaceAll("%3D", "=");
                    finalurl = finalurl.replaceAll("%26", "&");
                    Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                    intent.putExtra("videofilename", finalurl);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(),"Please Connect to Internet",Toast.LENGTH_LONG).show();
                }
            }
        });
        // Setting Netural "Cancel" Button
        alertDialog.setNeutralButton(getResources().getString(R.string.copyurl), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed Cancel button. Write Logic Here
                String finalurl ;
                finalurl=pathvideo;
                finalurl=finalurl.replaceAll("%3A",":");
                finalurl=finalurl.replaceAll("%2F","/");
                finalurl=finalurl.replaceAll("%3F","?");
                finalurl=finalurl.replaceAll("%3D","=");
                finalurl=finalurl.replaceAll("%26","&");
                ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("mainurlcopy",finalurl);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(),"Url Copied",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }
    public void downloadvideo(String pathvideo,int type)
        {



        if(pathvideo.contains(".mp4"))
        {
            if (type==0)
            {
                File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"Facebook Videos");
                directory.mkdirs();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pathvideo));
                int Number=pref.getFileName();
                request.allowScanningByMediaScanner();
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                File root = new File(Environment.getExternalStorageDirectory() + File.separator+"Facebook Videos");
                Uri path = Uri.withAppendedPath(Uri.fromFile(root), "Video-"+Number+".mp4");
                request.setDestinationUri(path);
                DownloadManager dm = (DownloadManager)getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
                if(downloadlist.contains(pathvideo))
                {
                    Toast.makeText(getActivity().getApplicationContext(),"The Video is Already Downloading",Toast.LENGTH_LONG).show();
                }
                else
                {
                    downloadlist.add(pathvideo);
                    dm.enqueue(request);
                    Toast.makeText(getActivity().getApplicationContext(),"Downloading Video-"+Number+".mp4",Toast.LENGTH_LONG).show();
                    Number++;
                    pref.setFileName(Number);
                }

            }else{
                File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"Facebook Videos");
                directory.mkdirs();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pathvideo));
                int Number=pref.getFileName();
                request.allowScanningByMediaScanner();
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                File root = new File(Environment.getExternalStorageDirectory() + File.separator+"Facebook Videos");
                Uri path = Uri.withAppendedPath(Uri.fromFile(root), "Video-"+Number+".mkv");
                request.setDestinationUri(path);
                DownloadManager dm = (DownloadManager)getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
                if(downloadlist.contains(pathvideo))
                {
                    Toast.makeText(getActivity().getApplicationContext(),"The Video is Already Downloading",Toast.LENGTH_LONG).show();
                }
                else
                {
                    downloadlist.add(pathvideo);
                    dm.enqueue(request);
                    Toast.makeText(getActivity().getApplicationContext(),"Downloading Video-"+Number+".mkv",Toast.LENGTH_LONG).show();
                    Number++;
                    pref.setFileName(Number);
                }

            }

        }
    }
    public ArrayList<String> getList() {
        return downloadlist;
    }


}