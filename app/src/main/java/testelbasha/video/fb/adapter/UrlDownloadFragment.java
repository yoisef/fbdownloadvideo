package testelbasha.video.fb.adapter;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import testelbasha.video.fb.PrefManager;
import testelbasha.video.fb.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override public void run() {
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                Log.d("TAG", "The interstitial wasn't loaded yet.");
                            }
                        }
                    });

                    downloadVideo(editText.getText().toString());
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
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
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
    private void downloadVideo(String pathvideo) {
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
    }
}