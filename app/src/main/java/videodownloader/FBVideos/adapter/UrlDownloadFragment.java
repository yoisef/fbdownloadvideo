package videodownloader.FBVideos.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import videodownloader.FBVideos.PrefManager;
import videodownloader.FBVideos.R;
import videodownloader.FBVideos.fbbrowse;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Adi on 25-03-2017.
 */
public class UrlDownloadFragment extends Fragment {
    ImageView imageButton;
    EditText editText;
    HomeFragment homeFragment;
    public InterstitialAd mInterstitialAd;
    public AdView mAdView;
    private PrefManager pref;
    private static final String ARG_POSITION = "position";
    public static UrlDownloadFragment newInstance(int position) {
        UrlDownloadFragment f = new UrlDownloadFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loadinterstial();
        interstiallistener();

        View rootView = inflater.inflate(R.layout.urldownload, container, false);
        homeFragment=new HomeFragment();
        imageButton= (ImageView) rootView.findViewById(R.id.download);
        editText= (EditText) rootView.findViewById(R.id.editText);
        pref = new PrefManager(getContext());
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().length()==0)
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Please Paste or Enter the Url",Toast.LENGTH_SHORT).show();
                }
                else
                {


                    View view=LayoutInflater.from(getActivity()).inflate(R.layout.choosequailty,null);
                    Button download,cancel;
                    final RadioGroup radiogroupp;
                    final RadioButton radioButton;
                    radiogroupp=view.findViewById(R.id.radiogroup);
                    radiogroupp.clearCheck();
                    download=view.findViewById(R.id.downloadvideo);
                    cancel=view.findViewById(R.id.cancelvideo);
                    final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                 builder

                            .setView(view);


                 final AlertDialog alertDialog=builder.create();
                 alertDialog.show();





                    download.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String urlvideo = editText.getText().toString();
                            if (urlvideo.contains("https://video")) {

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
                                        try {
                                            downloadVideo(editText.getText().toString(),0);
                                        }catch (Exception e)
                                        {
                                            Toast.makeText(getActivity(),"Make Sure you register with youracount in the app",Toast.LENGTH_LONG).show();
                                        }

                                        alertDialog.cancel();
                                        break;
                                    }
                                    default:{
                                        RadioButton radioButton
                                                = (RadioButton)radiogroupp
                                                .findViewById(selectedId);

                                        // Now display the value of selected item
                                        // by the Toast message
                                        alertDialog.cancel();
                                        try {
                                            downloadVideo(editText.getText().toString(),1);
                                        }catch (Exception e)
                                        {
                                            Toast.makeText(getActivity(),"Make Sure you register with youracount in the app",Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    }
                                }



                            }
                            else{
                                Intent intent=new Intent(getActivity(), fbbrowse.class);
                                intent.putExtra("url",editText.getText().toString());
                                startActivity(intent);
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
            }
        });
        return rootView;
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
    private void downloadVideo(String pathvideo,int type) {
        /*if (!(pathvideo.contains("fbcdn")))
        {
            Toast.makeText(getContext(), "Please Enter Only Video Url", Toast.LENGTH_LONG).show();
        }*/
        if(pathvideo.contains("story"))
        {
            homeFragment.getUrlfromUrlDownload(pathvideo);
        }
        else
        {
            if (type==0)
            {
                File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "FacebookVideos");
                directory.mkdirs();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pathvideo));
                request.allowScanningByMediaScanner();
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Facebook Videos");
                int Number = pref.getFileName();
                Uri path = Uri.withAppendedPath(Uri.fromFile(root), "Video-" + Number + ".mp4");
                request.setDestinationUri(path);
                DownloadManager dm = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
                ArrayList<String> urldownloadFragmentList = (new HomeFragment()).getList();
                if(urldownloadFragmentList.contains(pathvideo))
                {
                    Toast.makeText(getActivity().getApplicationContext(),"The Video is Already Downloading",Toast.LENGTH_LONG).show();
                }
                else
                {
                    urldownloadFragmentList.add(pathvideo);
                    dm.enqueue(request);
                    Toast.makeText(getActivity().getApplicationContext(),"Downloading Video-"+Number+".mp4",Toast.LENGTH_LONG).show();
                    Number++;
                    pref.setFileName(Number);
                }
            }
            else{
                File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "FacebookVideos");
                directory.mkdirs();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pathvideo));
                request.allowScanningByMediaScanner();
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Facebook Videos");
                int Number = pref.getFileName();
                Uri path = Uri.withAppendedPath(Uri.fromFile(root), "Video-" + Number + ".mkv");
                request.setDestinationUri(path);
                DownloadManager dm = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
                ArrayList<String> urldownloadFragmentList = (new HomeFragment()).getList();
                if(urldownloadFragmentList.contains(pathvideo))
                {
                    Toast.makeText(getActivity().getApplicationContext(),"The Video is Already Downloading",Toast.LENGTH_LONG).show();
                }
                else
                {
                    urldownloadFragmentList.add(pathvideo);
                    dm.enqueue(request);
                    Toast.makeText(getActivity().getApplicationContext(),"Downloading Video-"+Number+".mkv",Toast.LENGTH_LONG).show();
                    Number++;
                    pref.setFileName(Number);
                }
            }

        }
    }
}