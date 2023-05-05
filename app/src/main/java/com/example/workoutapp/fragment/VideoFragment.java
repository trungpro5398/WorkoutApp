package com.example.workoutapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workoutapp.databinding.VideoFragmentBinding;

public class VideoFragment extends Fragment {

    private VideoFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = VideoFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setUseWideViewPort(true);

        Bundle args = getArguments();
        String videoId = args != null ? args.getString("videoId") : null;
        if (videoId != null) {
            String videoHtml = "<html><body>" +
                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen video player\" frameborder=\"0\" allow=\"accelerometer autoplay clipboard-write encrypted-media gyroscope picture-in-picture\"></iframe>" +
                    "</body></html>";
            binding.webView.loadData(videoHtml, "text/html", "utf-8");
        } else {
            Toast.makeText(requireActivity(), "Video not found", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.webView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.webView.destroy();
        binding = null;
    }
}
