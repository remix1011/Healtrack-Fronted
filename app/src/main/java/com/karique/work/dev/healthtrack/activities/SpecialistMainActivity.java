package com.karique.work.dev.healthtrack.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.fragments.CaringFragment;
import com.karique.work.dev.healthtrack.fragments.MyPatientsFragment;
import com.karique.work.dev.healthtrack.fragments.PlacesFragment;
import com.karique.work.dev.healthtrack.fragments.ProfileFragment;
import com.karique.work.dev.healthtrack.fragments.ReportsFragment;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class SpecialistMainActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 146;
    public static int FRAGMENT_TYPE = MyPatientsFragment.FRAGMENT_TYPE_MY_PATIENTS;

    TextView toolbarTitleTextView;
    ProgressBar progressBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return navigateAccordingTo(item.getItemId());
        }
    };
    private BottomNavigationView navigation;

    MyPatientsFragment myPatientsFragment;
    //DemeanorFragment demeanorFragment;
    //PlacesFragment placesFragment;
    ProfileFragment profileFragment;

    SessionManager sessionManager;
    FloatingActionButton speedDialFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist_main);
        hide();

        inicializarControles();
        inicializarEventos();
        inicializarDatos();
    }

    private void inicializarControles() {
        toolbarTitleTextView = findViewById(R.id.toolbarTitleTextView);
        progressBar = findViewById(R.id.progressBar);
        navigation = findViewById(R.id.nav_view);
        speedDialFloatingActionButton = findViewById(R.id.speedDialFloatingActionButton);
    }
    private void inicializarEventos() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        speedDialFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionAgregarClickeada();
            }
        });
    }
    private void inicializarDatos() {
        sessionManager = SessionManager.getInstance(this);
        toolbarTitleTextView.setText(getText(R.string.app_name));
        navigateAccordingTo(R.id.navigation_mis_pacientes);
    }

    private boolean navigateAccordingTo(int id){
        try
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content,getFragmentFor(id))
                    .commit();
            return true;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }
    private Fragment getFragmentFor(int id) {
        if (id == R.id.navigation_mis_pacientes) {
            return getMyPatientsFragment();
        }
        //else if (id == R.id.navigation_conductas) {
        //    return getDemeanorFragment();
        //}
        //else if (id == R.id.navigation_ubicaciones) {
        //    return getPlacesFragment();
        //}
        else if (id == R.id.navigation_mi_perfil) {
            return getProfileFragment();
        }
        return null;
    }
    private MyPatientsFragment getMyPatientsFragment(){
        if (myPatientsFragment == null) {
            myPatientsFragment = new MyPatientsFragment();
            myPatientsFragment.setOnPatientClicked(new MyPatientsFragment.OnPatientClickedListener() {
                @Override
                public void OnPatientClicked(Patient patient) {
                    mostrarResponsibleMainActivity(patient);
                }
            });
            myPatientsFragment.setOnPatientLongClicked(new MyPatientsFragment.OnPatientLongClickedListener() {
                @Override
                public void OnPatientLongClicked(Patient patient) {
                    showDeletePatient(patient);
                }
            });
            myPatientsFragment.setOnProgressBarChanged(new MyPatientsFragment.OnProgressBarChanged() {
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

        toolbarTitleTextView.setText(getString(R.string.mis_pacientes));
        speedDialFloatingActionButton.show();
        FRAGMENT_TYPE = MyPatientsFragment.FRAGMENT_TYPE_MY_PATIENTS;
        return myPatientsFragment;
    }
//    private DemeanorFragment getDemeanorFragment(){
//        if (demeanorFragment == null) {
//            demeanorFragment = new DemeanorFragment();
//            demeanorFragment.setOnDemeanorHistoryClicked(new DemeanorFragment.OnDemeanorHistoryClickedListener() {
//                @Override
//                public void OnDemeanorHistoryClicked(DemeanorHistory news) {
//
//                }
//            });
//            demeanorFragment.setOnProgressBarChanged(new DemeanorFragment.OnProgressBarChanged() {
//                @Override
//                public void OnProgressBarVisible() {
//                    progressBar.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void OnProgressBarHide() {
//                    progressBar.setVisibility(View.GONE);
//                }
//            });
//        }
//
//        toolbarTitleTextView.setText("Conductas");
//        speedDialFloatingActionButton.show();
//        FRAGMENT_TYPE = DemeanorFragment.FRAGMENT_TYPE_DEMEANOR;
//        return demeanorFragment;
//    }
//    private PlacesFragment getPlacesFragment() {
//        if (placesFragment == null) {
//            placesFragment = new PlacesFragment();
//            placesFragment.setOnProgressBarChanged(new PlacesFragment.OnProgressBarChanged() {
//                @Override
//                public void OnProgressBarVisible() {
//                    progressBar.setVisibility(View.VISIBLE);
//                }
//                @Override
//                public void OnProgressBarHide() {
//                    progressBar.setVisibility(View.GONE);
//                }
//            });
//        }
//
//        toolbarTitleTextView.setText("Lugares");
//        speedDialFloatingActionButton.hide();
//        FRAGMENT_TYPE = PlacesFragment.FRAGMENT_TYPE_PLACES;
//        return placesFragment;
//    }
    private ProfileFragment getProfileFragment(){
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
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
        speedDialFloatingActionButton.hide();
        FRAGMENT_TYPE = ProfileFragment.FRAGMENT_TYPE_PROFILE;
        return profileFragment;
    }

    private void opcionAgregarClickeada() {
        switch (FRAGMENT_TYPE) {
            case MyPatientsFragment.FRAGMENT_TYPE_MY_PATIENTS:
                mostrarAddSpecialistByResponsable();
                break;
            case CaringFragment.FRAGMENT_TYPE_CARING:
                //mostrarAddDriverActivity();
                break;
            case PlacesFragment.FRAGMENT_TYPE_PLACES:
                //mostrarAddComponentActivity();
                break;
        }
    }
    //private void mostrarCarDetailsActivity(Car car) {
    //    Intent intent = new Intent(this, CarDetailsActivity.class);
    //    intent.putExtra("car", car.toBundle());
    //    startActivityForResult(intent, CarDetailsActivity.REQUEST_CODE);
    //    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up);
    //}
    //private void mostrarDriverDetailsActivity(Driver driver) {
    //    Intent intent = new Intent(this, DriverDetailsActivity.class);
    //    intent.putExtra("driver", driver.toBundle());
    //    startActivityForResult(intent, DriverDetailsActivity.REQUEST_CODE);
    //    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up);
    //}
    private void mostrarAddSpecialistByResponsable() {
        Intent intent = new Intent(this, AddSpecialistByResponsableActivity.class);
        startActivityForResult(intent, AddSpecialistByResponsableActivity.REQUEST_CODE);
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarResponsibleMainActivity(Patient patient) {
        Intent intent = new Intent(this, SpecialistGoToResponsibleMainActivity.class);
        intent.putExtras(patient.toBundle());
        startActivityForResult(intent, SpecialistGoToResponsibleMainActivity.REQUEST_CODE);
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarResetPasswordInAppActivity() {
        Intent intent = new Intent(this, ResetPasswordInAppActivity.class);
        startActivityForResult(intent, ResetPasswordInAppActivity.REQUEST_CODE);
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void showDeletePatient(Patient patient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que desea eliminar este paciente de su lista?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteResponsableBySpecialist(patient);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void deleteResponsableBySpecialist(Patient patient){
        progressBar.setVisibility(View.VISIBLE);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("idUserSpecialist", sessionManager.getiduser());
            jsonObject.put("idUserResponsable", patient.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = HealthTrackApi.USERS_DELETE_PATIENT;
        AndroidNetworking.delete(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mostrarToast("Eliminado correctamente");
                        progressBar.setVisibility(View.GONE);
                        myPatientsFragment.getPatients();
                    }

                    @Override
                    public void onError(ANError anError) {
                        handleError(anError);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
    private void mostrarToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    private void handleError(ANError anError) {
        if (anError.getErrorCode() == 404){
            Toast.makeText(this, "Error: datos incorrectos" + anError.getMessage(), Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Error al realizar la petición" + anError.getMessage(), Toast.LENGTH_LONG).show();
        }
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
        //una vez agregado el paciente, los listo
        if (requestCode == AddSpecialistByResponsableActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                myPatientsFragment.getPatients();
            }
        }
        ////una vez agregado el carro, listo los carros de nuevo
        //if (requestCode == AddCarActivity.REQUEST_CODE) {
        //    if (resultCode == Activity.RESULT_OK) {
        //        reportsFragment.getCars();
        //    }
        //}
    }

    @Override
    public void onBackPressed() {
        if (FRAGMENT_TYPE != MyPatientsFragment.FRAGMENT_TYPE_MY_PATIENTS) {
            navigation.setSelectedItemId(R.id.navigation_mis_pacientes);
            navigateAccordingTo(R.id.navigation_mis_pacientes);
        }
        else super.onBackPressed();
    }
}