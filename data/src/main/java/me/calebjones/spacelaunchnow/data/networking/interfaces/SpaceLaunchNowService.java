package me.calebjones.spacelaunchnow.data.networking.interfaces;

import me.calebjones.spacelaunchnow.data.BuildConfig;
import me.calebjones.spacelaunchnow.data.models.main.Launch;
import me.calebjones.spacelaunchnow.data.networking.responses.base.AgencyResponse;
import me.calebjones.spacelaunchnow.data.networking.responses.base.LaunchListResponse;
import me.calebjones.spacelaunchnow.data.networking.responses.base.LaunchResponse;
import me.calebjones.spacelaunchnow.data.networking.responses.base.OrbiterResponse;
import me.calebjones.spacelaunchnow.data.networking.responses.base.VehicleResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpaceLaunchNowService {

    String version = "3.2.0";

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/orbiters/")
    Call<OrbiterResponse> getOrbiter();

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/agencies/")
    Call<AgencyResponse> getAgencies(@Query("featured") boolean featured, @Query("mode") String mode);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/agencies/")
    Call<AgencyResponse> getAgenciesWithOrbiters(@Query("orbiters") boolean orbiters);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launchers_string/")
    Call<VehicleResponse> getVehiclesByAgency(@Query("launch_agency__name") String agency);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launchers_string/")
    Call<VehicleResponse> getVehicle(@Query("full_name") String vehicle);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/upcoming/")
    Call<LaunchResponse> getUpcomingLaunches(@Query("limit") int amount, @Query("offset") int offset,
                                             @Query("mode") String mode);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/upcoming/")
    Call<LaunchResponse> getUpcomingLaunches(@Query("limit") int amount, @Query("offset") int offset,
                                             @Query("mode") String mode, @Query("search") String search);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/upcoming/")
    Call<LaunchResponse> getUpcomingLaunches(@Query("limit") int amount, @Query("offset") int offset,
                                             @Query("mode") String mode, @Query("search") String search,
                                             @Query("lsp__name") String lspName,
                                             @Query("launcher_config__id") Integer launcherId,
                                             @Query("lsp__ids") String lspIds,
                                             @Query("location__ids") String locationIds);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/upcoming/")
    Call<LaunchListResponse> getUpcomingLaunchesMini(@Query("limit") int amount, @Query("offset") int offset,
                                                     @Query("mode") String mode, @Query("search") String search,
                                                     @Query("lsp__name") String lspName, @Query("serial_number") String serialNumber,
                                                     @Query("launcher_config__id") Integer launcherId);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/upcoming/")
    Call<LaunchResponse> getUpcomingLaunches(@Query("limit") int amount, @Query("offset") int offset,
                                             @Query("mode") String mode, @Query("search") String search,
                                             @Query("lsp__name") String lspName);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/upcoming/")
    Call<LaunchResponse> getUpcomingLaunches(@Query("limit") int amount, @Query("offset") int offset,
                                             @Query("mode") String mode,
                                             @Query("launcher_config__id") Integer launcherId);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/upcoming/")
    Call<LaunchResponse> getUpcomingLaunches(@Query("limit") int amount, @Query("offset") int offset,
                                             @Query("mode") String mode, @Query("search") String search,
                                             @Query("lsp__name") String lspName, @Query("lsp__id") Integer lspId,
                                             @Query("launcher_config__id") Integer launcherId);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/previous/")
    Call<LaunchResponse> getPreviousLaunches(@Query("limit") int amount, @Query("offset") int offset,
                                             @Query("mode") String mode);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/previous/")
    Call<LaunchResponse> getPreviousLaunches(@Query("limit") int amount, @Query("offset") int offset,
                                             @Query("mode") String mode, @Query("search") String search);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/previous/")
    Call<LaunchResponse> getPreviousLaunchesByLspID(@Query("limit") int amount, @Query("offset") int offset,
                                                    @Query("mode") String mode, @Query("search") String search,
                                                    @Query("lsp__id") Integer lspId);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/previous/")
    Call<LaunchResponse> getPreviousLaunchesByLspName(@Query("limit") int amount, @Query("offset") int offset,
                                                      @Query("mode") String mode, @Query("search") String search,
                                                      @Query("lsp__name") String lspName);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/previous/")
    Call<LaunchResponse> getPreviousLaunches(@Query("limit") int amount, @Query("offset") int offset,
                                             @Query("mode") String mode, @Query("search") String search,
                                             @Query("lsp__name") String lspName, @Query("launcher_config__id") Integer lspId);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/previous/")
    Call<LaunchListResponse> getPreviousLaunchesMini(@Query("limit") int amount, @Query("offset") int offset,
                                                     @Query("mode") String mode, @Query("search") String search,
                                                     @Query("lsp__name") String lspName, @Query("serial_number") String serialNumber,
                                                     @Query("launcher_config__id") Integer lspId);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/previous/")
    Call<LaunchResponse> getPreviousLaunchesByLauncherID(@Query("limit") int amount, @Query("offset") int offset,
                                                         @Query("mode") String mode, @Query("search") String search,
                                                         @Query("launcher_config__id") Integer launcherId);


    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/{id}/")
    Call<Launch> getLaunchById(@Path("id") int id, @Query("mode") String mode);

    @Headers({"User-Agent: SpaceLaunchNow-" + BuildConfig.VERSION_NAME})
    @GET(version + "/launch/")
    Call<LaunchResponse> getLaunchesByDate(@Query("limit") int amount, @Query("offset") int offset,
                                           @Query("net__lte") String startDate, @Query("net__gte") String endDate,
                                           @Query("launcher_config__id") Integer launcherId);

}