package videodownloader.FBVideos.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import videodownloader.FBVideos.R;

public class homeexplanitionfrag extends Fragment {

    View mainView;
    private static final String ARG_POSITION = "position";
    RecyclerView explantionrecycle;



    public static homeexplanitionfrag newInstance(int position) {
        homeexplanitionfrag f = new homeexplanitionfrag();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView=inflater.inflate(R.layout.explantionfragment,container,false);

        return mainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        explantionrecycle=mainView.findViewById(R.id.recycleexp);
        explantionrecycle.setLayoutManager(new GridLayoutManager(getActivity(),2));
        explantionrecycle.setAdapter(new expadapter(getActivity()));
    }
}
