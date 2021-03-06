package me.calebjones.spacelaunchnow.content.data.details;


import android.content.Context;

import io.realm.Realm;
import me.calebjones.spacelaunchnow.content.data.DataSaver;
import me.calebjones.spacelaunchnow.content.data.callbacks.Callbacks;
import me.calebjones.spacelaunchnow.data.models.main.Launch;
import me.calebjones.spacelaunchnow.data.networking.DataClient;
import me.calebjones.spacelaunchnow.data.networking.error.ErrorUtil;
import me.calebjones.spacelaunchnow.data.networking.error.LibraryError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class DetailsLaunchDataLoader {

    private DataSaver dataSaver;
    private Context context;

    public DetailsLaunchDataLoader(Context context) {
        this.context = context;
        this.dataSaver = new DataSaver(context);
    }

    public void getLaunch(int id, Realm realm, Callbacks.DetailsNetworkCallback networkCallback) {
        Timber.i("Running getLaunch");

        DataClient.getInstance().getLaunchById(id, new Callback<Launch>() {
            @Override
            public void onResponse(Call<Launch> call, Response<Launch> response) {
                if (response.isSuccessful()) {
                    Launch launch = response.body();
                    Timber.v("Launch: %s", launch.getName());
                    dataSaver.saveLaunchToRealm(launch);
                    networkCallback.onSuccess(launch);
                } else {
                    LibraryError error = ErrorUtil.parseLibraryError(response);
                    if (error.getMessage() != null && error.getMessage().contains("None found")) {
                        final Launch launch = realm.where(Launch.class).equalTo("id", id).findFirst();
                        if (launch != null) {
                            realm.executeTransaction(realm -> launch.deleteFromRealm());
                        }
                        networkCallback.onLaunchDeleted();
                    } else {
                        networkCallback.onNetworkFailure(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<Launch> call, Throwable t) {
                networkCallback.onFailure(t);
            }
        });
    }

}
