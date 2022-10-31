package com.karique.work.dev.healthtrack.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.karique.work.dev.healthtrack.BuildConfig;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.fragments.CaringFragment;
import com.karique.work.dev.healthtrack.fragments.PlacesFragment;
import com.karique.work.dev.healthtrack.fragments.ProfileFragment;
import com.karique.work.dev.healthtrack.fragments.ReportsFragment;
import com.karique.work.dev.healthtrack.models.CaloriesHistory;
import com.karique.work.dev.healthtrack.models.DailyReport;
import com.karique.work.dev.healthtrack.models.DistanceHistory;
import com.karique.work.dev.healthtrack.models.HeartbeatHistory;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.models.PlaceHistory;
import com.karique.work.dev.healthtrack.models.SleepHistory;
import com.karique.work.dev.healthtrack.models.StepsHistory;
import com.karique.work.dev.healthtrack.models.WeightHistory;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.karique.work.dev.healthtrack.util.FuncionesUtiles;

import org.json.JSONObject;

public class ResponsibleMainActivity extends AppCompatActivity {
    public static final String TAG = "Sleep";
    public static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1001;
    public static final int GOOGLE_FIT_ACTIVITY_RECOGNITION_PERMISSIONS_REQUEST_CODE = 1002;
    public static final int GPS_PERMISSIONS_REQUEST_CODE = 1003;

    public static int REQUEST_CODE = 145;
    public static int FRAGMENT_TYPE = ReportsFragment.FRAGMENT_TYPE_REPORTS;

    private AppCompatImageButton backAppCompatImageButton;
    private TextView toolbarTitleTextView;
    private ProgressBar progressBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return navigateAccordingTo(item.getItemId());
        }
    };
    private BottomNavigationView navigation;

    private ReportsFragment reportsFragment;
    private CaringFragment caringFragment;
    private PlacesFragment placesFragment;
    private ProfileFragment profileFragment;

    private SessionManager sessionManager;
    private FloatingActionButton speedDialFloatingActionButton;

    private Patient patient;

    private LocationManager locationManager;
    private CardView syncLocationCardView;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsible_main);
        hide();

        inicializarControles();
        inicializarEventos();
        inicializarDatos();

        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

    }

    private void inicializarControles() {
        backAppCompatImageButton = findViewById(R.id.backAppCompatImageButton);
        toolbarTitleTextView = findViewById(R.id.toolbarTitleTextView);
        progressBar = findViewById(R.id.progressBar);
        navigation = findViewById(R.id.nav_view);
        speedDialFloatingActionButton = findViewById(R.id.speedDialFloatingActionButton);
        syncLocationCardView = findViewById(R.id.syncLocationCardView);
    }
    private void inicializarEventos() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        speedDialFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionAgregarClickeada();
            }
        });
        backAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        syncLocationCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!permissionGpsApproved()) {
                    ActivityCompat.requestPermissions(
                            ResponsibleMainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            GPS_PERMISSIONS_REQUEST_CODE
                    );
                } else {
                    validacionPreviaGPS();
                }
            }
        });
    }
    private void inicializarDatos() {
        sessionManager = SessionManager.getInstance(this);
        Bundle bundle = getIntent().getExtras();
        patient = bundle == null ? null : Patient.from(bundle);

        toolbarTitleTextView.setText(getText(R.string.app_name));
        if (patient == null)
            backAppCompatImageButton.setVisibility(View.GONE);
        else
            backAppCompatImageButton.setVisibility(View.VISIBLE);
        navigateAccordingTo(R.id.navigation_reportes);

//        fitnessOptions = FitnessOptions.builder()
//                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
//                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
//                .build();
//
//        account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);
//
//        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
//            GoogleSignIn.requestPermissions(
//                    this, // your activity
//                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, // e.g. 1
//                    account,
//                    fitnessOptions);
//        }
//        else {
//            //accessGoogleFit();
//        }
    }

    private boolean navigateAccordingTo(int id) {
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, getFragmentFor(id))
                    .commit();
            return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }
    private Fragment getFragmentFor(int id) {
        if (id == R.id.navigation_reportes) {
            return getReportsFragment();
        } else if (id == R.id.navigation_cuidado) {
            return getCaringFragment();
        } else if (id == R.id.navigation_ubicaciones) {
            return getPlacesFragment();
        } else if (id == R.id.navigation_mi_perfil) {
            return getProfileFragment();
        }
        return null;
    }
    private ReportsFragment getReportsFragment() {
        if (reportsFragment == null) {
            reportsFragment = new ReportsFragment();
            reportsFragment.setPatient(patient);
            reportsFragment.setOnProgressBarChanged(new ReportsFragment.OnProgressBarChanged() {
                @Override
                public void OnProgressBarVisible() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void OnProgressBarHide() {
                    progressBar.setVisibility(View.GONE);
                }
            });
            reportsFragment.setOnStepHistoryClicked(new ReportsFragment.OnStepHistoryClickedListener() {
                @Override
                public void OnStepHistoryClicked() {
                    mostrarStepsReportActivity();
                }
            });
            reportsFragment.setOnDistanceHistoryClicked(new ReportsFragment.OnDistanceHistoryClickedListener() {
                @Override
                public void OnDistanceHistoryClicked() {
                    mostrarDistanceReportActivity();
                }
            });
            reportsFragment.setOnCaloriesHistoryClicked(new ReportsFragment.OnCaloriesHistoryClickedListener() {
                @Override
                public void OnCaloriesHistoryClicked() {
                    mostrarCaloriesReportActivity();
                }
            });
            reportsFragment.setOnHeartbeatHistoryClicked(new ReportsFragment.OnHeartbeatHistoryClickedListener() {
                @Override
                public void OnHeartbeatHistoryClicked() {
                    mostrarHeartBeatReportActivity();
                }
            });
            reportsFragment.setOnSleepHistoryClicked(new ReportsFragment.OnSleepHistoryClickedListener() {
                @Override
                public void OnSleepHistoryClicked() {
                    mostrarSleepReportActivity();
                }
            });
            reportsFragment.setOnWeightHistoryClicked(new ReportsFragment.OnWeightHistoryClickedListener() {
                @Override
                public void OnWeightHistoryClicked() {
                    mostrarWeightReportActivity();
                }
            });
            reportsFragment.setOnOximetryClicked(new ReportsFragment.OnOximetryClickedListener() {
                @Override
                public void OnOximetryClicked() {
                    mostrarOximetryReportActivity();
                }
            });
        }

        toolbarTitleTextView.setText("Hoy");
        if (patient != null)
            toolbarTitleTextView.setText(patient.getFullName());

        speedDialFloatingActionButton.hide();
        setPaddingForSync(16, 16, 16, 16);
        FRAGMENT_TYPE = ReportsFragment.FRAGMENT_TYPE_REPORTS;
        return reportsFragment;
    }
    private CaringFragment getCaringFragment() {
        if (caringFragment == null) {
            caringFragment = new CaringFragment();
            caringFragment.setPatient(patient);
            caringFragment.setOnCaringEnviromentClicked(new CaringFragment.OnCaringEnviromentClickedListener() {
                @Override
                public void OnCaringEnviromentClicked() {
                    mostrarCaringEnvironmentActivity();
                }
            });
            caringFragment.setOnDiseaseCategoryClicked(new CaringFragment.OnDiseaseCategoryClickedListener() {
                @Override
                public void OnDiseaseCategoryClicked() {
                    mostrarDiseaseCategoryActivity();
                }
            });
            caringFragment.setOnTreatmentHistoryClicked(new CaringFragment.OnTreatmentHistoryClickedListener() {
                @Override
                public void OnTreatmentHistoryClicked() {
                    mostrarTreatmentActivity();
                }
            });
            caringFragment.setOnDemeanorClicked(new CaringFragment.OnDemeanorClickedListener() {
                @Override
                public void OnDemeanorClicked() {
                    mostrarDemeanorActivity();
                }
            });
            caringFragment.setOnRecommendationsClicked(new CaringFragment.OnRecommendationsClickedListener() {
                @Override
                public void OnRecommendationsClicked() {
                    mostrarRecommendationsActivity();
                }
            });
            caringFragment.setOnProgressBarChanged(new CaringFragment.OnProgressBarChanged() {
                @Override
                public void OnProgressBarVisible() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void OnProgressBarHide() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        toolbarTitleTextView.setText("Cuidado del paciente");
        if (patient != null)
            toolbarTitleTextView.setText(patient.getFullName());

        speedDialFloatingActionButton.hide();
        setPaddingForSync(16, 16, 16, 16);
        FRAGMENT_TYPE = CaringFragment.FRAGMENT_TYPE_CARING;
        return caringFragment;
    }
    private PlacesFragment getPlacesFragment() {
        if (placesFragment == null) {
            placesFragment = new PlacesFragment();
            placesFragment.setPatient(patient);
            placesFragment.setOnProgressBarChanged(new PlacesFragment.OnProgressBarChanged() {
                @Override
                public void OnProgressBarVisible() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void OnProgressBarHide() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        toolbarTitleTextView.setText("Lugares");
        if (patient != null)
            toolbarTitleTextView.setText(patient.getFullName());

        speedDialFloatingActionButton.hide();
        setPaddingForSync(16, 16, 16, 96);
        FRAGMENT_TYPE = PlacesFragment.FRAGMENT_TYPE_PLACES;
        return placesFragment;
    }
    private ProfileFragment getProfileFragment() {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
            profileFragment.setPatient(patient);
            profileFragment.setOnEditPasswordClicked(new ProfileFragment.OnEditPasswordClickedListener() {
                @Override
                public void OnEditPasswordClicked() {
                    mostrarResetPasswordInAppActivity();
                }
            });
            profileFragment.setOnProgressBarChanged(new ProfileFragment.OnProgressBarChanged() {
                @Override
                public void OnProgressBarVisible() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void OnProgressBarHide() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        toolbarTitleTextView.setText(getString(R.string.mi_perfil));
        if (patient != null)
            toolbarTitleTextView.setText("Detalles del usuario");

        speedDialFloatingActionButton.hide();
        setPaddingForSync(16, 16, 16, 16);
        FRAGMENT_TYPE = ProfileFragment.FRAGMENT_TYPE_PROFILE;
        return profileFragment;
    }

    private void opcionAgregarClickeada() {
        switch (FRAGMENT_TYPE) {
            case ReportsFragment.FRAGMENT_TYPE_REPORTS:
                //mostrarAddCarActivity();
                break;
            case CaringFragment.FRAGMENT_TYPE_CARING:
                //mostrarAddDriverActivity();
                break;
            case PlacesFragment.FRAGMENT_TYPE_PLACES:
                //mostrarAddComponentActivity();
                break;
        }
    }
    private void mostrarCaringEnvironmentActivity() {
        Intent intent = new Intent(this, CaringEnvironmentActivity.class);
        if (patient != null)
            intent.putExtras(patient.toBundle());
        startActivityForResult(intent, CaringEnvironmentActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarDiseaseCategoryActivity() {
        Intent intent = new Intent(this, DiseaseCategoryActivity.class);
        if (patient != null)
            intent.putExtras(patient.toBundle());
        startActivityForResult(intent, DiseaseCategoryActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarTreatmentActivity() {
        Intent intent = new Intent(this, TreatmentActivity.class);
        if (patient != null)
            intent.putExtras(patient.toBundle());
        startActivityForResult(intent, TreatmentActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarDemeanorActivity() {
        Intent intent = new Intent(this, DemeanorActivity.class);
        if (patient != null)
            intent.putExtras(patient.toBundle());
        startActivityForResult(intent, DemeanorActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarRecommendationsActivity() {
        Intent intent = new Intent(this, RecommendationsActivity.class);
        if (patient != null)
            intent.putExtras(patient.toBundle());
        startActivityForResult(intent, RecommendationsActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarResetPasswordInAppActivity() {
        Intent intent = new Intent(this, ResetPasswordInAppActivity.class);
        startActivityForResult(intent, ResetPasswordInAppActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    private void mostrarStepsReportActivity() {
        Intent intent = new Intent(this, StepsReportActivity.class);
        intent.putExtra("idUser", sessionManager.getiduser());
        startActivityForResult(intent, StepsReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarDistanceReportActivity() {
        Intent intent = new Intent(this, DistanceReportActivity.class);
        intent.putExtra("idUser", sessionManager.getiduser());
        startActivityForResult(intent, DistanceReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarCaloriesReportActivity() {
        Intent intent = new Intent(this, CaloriesReportActivity.class);
        intent.putExtra("idUser", sessionManager.getiduser());
        startActivityForResult(intent, CaloriesReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarHeartBeatReportActivity() {
        Intent intent = new Intent(this, HeartBeatReportActivity.class);
        intent.putExtra("idUser", sessionManager.getiduser());
        startActivityForResult(intent, HeartBeatReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarSleepReportActivity() {
        Intent intent = new Intent(this, SleepReportActivity.class);
        intent.putExtra("idUser", sessionManager.getiduser());
        startActivityForResult(intent, SleepReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarWeightReportActivity() {
        Intent intent = new Intent(this, WeightReportActivity.class);
        intent.putExtra("idUser", sessionManager.getiduser());
        startActivityForResult(intent, WeightReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarOximetryReportActivity() {
        Intent intent = new Intent(this, OximetryReportActivity.class);
        intent.putExtra("idUser", sessionManager.getiduser());
        startActivityForResult(intent, OximetryReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    private void validacionPreviaGPS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mostrarToast("Por favor active su GPS.");

            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
            }
        }
    }
    private void getLocation() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                guardarUbicacionActual(
                                        location.getLatitude(),
                                        location.getLongitude()
                                );
                            }
                            else {
                                mostrarToast("No fue posible obtener su ubicación (Nula).");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mostrarToast("No fue posible obtener su ubicación.");
                        }
                    });
        }

        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        //        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, GPS_PERMISSIONS_REQUEST_CODE);
        //} else {
        //    Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //    Location location2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //    Location location3 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);
//
        //    if (location1 != null) {
        //        guardarUbicacionActual(location1.getLatitude(), location1.getLongitude());
        //    } else  if (location2 != null) {
        //        guardarUbicacionActual(location2.getLatitude(), location2.getLongitude());
        //    } else  if (location3 != null) {
        //        guardarUbicacionActual(location3.getLatitude(), location3.getLongitude());
        //    }
        //    else {
        //        mostrarToast("No fue posible obtener su ubicación.");
        //    }
        //}
    }
    private void guardarUbicacionActual(double lat, double lng) {
        progressBar.setVisibility(View.VISIBLE);

        PlaceHistory placeHistory = new PlaceHistory(
                String.valueOf(lat),
                String.valueOf(lng),
                sessionManager.getiduser()
        );
        JSONObject placeJsonObject = placeHistory.toJsonObject();

        AndroidNetworking.post(HealthTrackApi.PLACES_HISTORY_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(placeJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        mostrarToast("Sincornización correcta.");
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        mostrarToast(
                                "Error al subir información al servidor: "
                                        + anError.getErrorDetail()
                        );
                    }
                });
    }
    private void checkPermissionsAndRun() {
        if (permissionFitApproved()) {
            reportsFragment.getStepsGoogleFit();
        } else {
            requestRuntimePermissions();
        }
    }
    private void requestRuntimePermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
        );

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Requesting permission 1");
            Snackbar.make(findViewById(R.id.content),
                            "Es necesario que brinde los permisos de actividad física.",
                            Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            makeRequest();
                        }
                    }).show();
        } else {
            Log.i(TAG, "Requesting permission 2");
            makeRequest();
        }
    }
    private void makeRequest() {
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACTIVITY_RECOGNITION },
                GOOGLE_FIT_ACTIVITY_RECOGNITION_PERMISSIONS_REQUEST_CODE
        );
    }
    private boolean permissionFitApproved() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION);
        }
        return true;
    }
    private boolean permissionGpsApproved() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
    }
    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
    public int dpToPx(int dp) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }
    public void setPaddingForSync(int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) syncLocationCardView.getLayoutParams();
        params.leftMargin = dpToPx(left);
        params.topMargin = dpToPx(top);
        params.rightMargin = dpToPx(right);
        params.bottomMargin = dpToPx(bottom);
    }
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //una vez agregado un cuidado, listar de nuevo
        if (requestCode == CaringEnvironmentActivity.REQUEST_CODE
        || requestCode == DiseaseCategoryActivity.REQUEST_CODE
        || requestCode == TreatmentActivity.REQUEST_CODE
        || requestCode == DemeanorActivity.REQUEST_CODE
        || requestCode == RecommendationsActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                caringFragment.getUserCaringDetails();
            }
        }

        //una vez agregado un spo2, listar de nuevo
        if (requestCode == OximetryReportActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (reportsFragment != null)
                    reportsFragment.getOximetryToday();
            }
        }

        //permiso de google fit
        if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
                checkPermissionsAndRun();
            else mostrarToast("No se aceptaron todos los permisos de acceso a google fit.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (permissionGpsApproved()) {
            validacionPreviaGPS();
        }

        if (grantResults.length == 0)
            mostrarToast("No se aceptaron todos los permisos.");
        else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            reportsFragment.getStepsGoogleFit();
        }
        else  {
            Snackbar.make(findViewById(R.id.content), "Es necesario que brinde los permisos de actividad física.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Aceptar permisos", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("com.karique.work.dev.healthtrack",BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);
    }

    @Override
    public void onBackPressed() {
        if (FRAGMENT_TYPE != ReportsFragment.FRAGMENT_TYPE_REPORTS) {
            navigation.setSelectedItemId(R.id.navigation_reportes);
            navigateAccordingTo(R.id.navigation_reportes);
        }
        else super.onBackPressed();
    }
}