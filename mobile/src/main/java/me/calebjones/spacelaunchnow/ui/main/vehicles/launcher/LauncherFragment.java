package me.calebjones.spacelaunchnow.ui.main.vehicles.launcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.kinst.jakub.view.SimpleStatefulLayout;
import me.calebjones.spacelaunchnow.R;
import me.calebjones.spacelaunchnow.common.RetroFitFragment;
import me.calebjones.spacelaunchnow.data.models.main.Agency;
import me.calebjones.spacelaunchnow.data.networking.error.ErrorUtil;
import me.calebjones.spacelaunchnow.data.networking.interfaces.SpaceLaunchNowService;
import me.calebjones.spacelaunchnow.data.networking.responses.base.AgencyResponse;
import me.calebjones.spacelaunchnow.ui.launcher.LauncherDetailActivity;
import me.calebjones.spacelaunchnow.utils.analytics.Analytics;
import me.calebjones.spacelaunchnow.utils.OnItemClickListener;
import me.calebjones.spacelaunchnow.utils.views.SnackbarHandler;
import okhttp3.CacheControl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LauncherFragment extends RetroFitFragment implements SwipeRefreshLayout.OnRefreshListener {

    private VehicleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Agency> items = new ArrayList<>();
    private Context context;
    private View view;
    @BindView(R.id.stateful_view)
    SimpleStatefulLayout statefulView;
    @BindView(R.id.vehicle_detail_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.vehicle_coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private Unbinder unbinder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        adapter = new VehicleAdapter(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_launch_vehicles, container, false);
        unbinder = ButterKnife.bind(this, view);

        mRecyclerView = view.findViewById(R.id.vehicle_detail_list);
        coordinatorLayout = view.findViewById(R.id.vehicle_coordinator);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (getResources().getBoolean(R.bool.landscape) && getResources().getBoolean(R.bool.isTablet)) {
            layoutManager = new GridLayoutManager(context, 3);
        } else if (getResources().getBoolean(R.bool.landscape)  || getResources().getBoolean(R.bool.isTablet)) {
            layoutManager = new GridLayoutManager(context, 2);
        } else {
            layoutManager = new LinearLayoutManager(context);
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        adapter.setOnItemClickListener(recyclerRowClickListener);
        mRecyclerView.setAdapter(adapter);
        Timber.v("Returning view.");
        statefulView.showProgress();
        statefulView.setOfflineRetryOnClickListener(v -> loadJSON(false));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.v("onDestroyView");
        swipeRefreshLayout.setOnRefreshListener(null);
        unbinder.unbind();
    }



    @Override
    public void onResume() {
        super.onResume();
        Timber.v("onResume");
        new Handler().postDelayed(() -> loadJSON(false), 100);
    }

    private void loadJSON(boolean forceRefresh) {
        Timber.v("Loading vehicles...");
        showLoading();

        SpaceLaunchNowService request = getSpaceLaunchNowRetrofit().create(SpaceLaunchNowService.class);
        Call<AgencyResponse> call = request.getAgencies(true, "detailed");
        call.enqueue(new Callback<AgencyResponse>() {
            @Override
            public void onResponse(Call<AgencyResponse> call, Response<AgencyResponse> response) {
                Timber.v("onResponse");
                if (response.raw().cacheResponse() != null) {
                    Timber.v("Response pulled from cache.");
                }

                if (response.raw().networkResponse() != null) {
                    Timber.v("Response pulled from network.");
                }

                if (response.isSuccessful()) {
                    AgencyResponse jsonResponse = response.body();
                    Timber.v("Success %s", response.message());
                    items = jsonResponse.getAgencies();
                    adapter.addItems(items);
                    statefulView.showContent();
                    Analytics.getInstance().sendNetworkEvent("LAUNCHER_INFORMATION", call.request().url().toString(), true);

                } else {
                    statefulView.showEmpty();
                    Timber.e(ErrorUtil.parseSpaceLaunchNowError(response).message());
                    SnackbarHandler.showErrorSnackbar(context, coordinatorLayout, ErrorUtil.parseSpaceLaunchNowError(response).message());
                }
                hideLoading();
            }

            @Override
            public void onFailure(Call<AgencyResponse> call, Throwable t) {
                Timber.e(t.getMessage());
                hideLoading();
                statefulView.showOffline();
                SnackbarHandler.showErrorSnackbar(context, coordinatorLayout, t.getLocalizedMessage());
                Analytics.getInstance().sendNetworkEvent("VEHICLE_INFORMATION", call.request().url().toString(), false, t.getLocalizedMessage());
            }
        });
    }

    private void hideLoading() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void showLoading() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    private OnItemClickListener recyclerRowClickListener = (v, position) -> {
        Analytics.getInstance().sendButtonClicked("Launcher clicked", items.get(position).getName());
        Gson gson = new Gson();
        String jsonItem = gson.toJson(items.get(position));

        Intent intent = new Intent(getActivity(), LauncherDetailActivity.class);
        intent.putExtra("name", items.get(position).getName());
        intent.putExtra("json", jsonItem);
        startActivity(intent);
    };

    @Override
    public void onRefresh() {
        Analytics.getInstance().sendButtonClicked("Launcher Refresh");
        loadJSON(true);
    }
}
