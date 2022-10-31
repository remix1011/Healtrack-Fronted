package com.karique.work.dev.healthtrack.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.fragments.CaringFragment;
import com.karique.work.dev.healthtrack.fragments.PlacesFragment;
import com.karique.work.dev.healthtrack.fragments.ProfileFragment;
import com.karique.work.dev.healthtrack.fragments.ReportsFragment;
import com.karique.work.dev.healthtrack.fragments.RisponsibleReportsFragment;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.session.SessionManager;

public class SpecialistGoToResponsibleMainActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 163;
    public static int FRAGMENT_TYPE = RisponsibleReportsFragment.FRAGMENT_TYPE_REPORTS;

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

    private RisponsibleReportsFragment reportsFragment;
    private CaringFragment caringFragment;
    private PlacesFragment placesFragment;
    private ProfileFragment profileFragment;

    private SessionManager sessionManager;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist_go_to_responsible_main);
        hide();

        inicializarControles();
        inicializarEventos();
        inicializarDatos();
    }

    private void inicializarControles() {
        backAppCompatImageButton = findViewById(R.id.backAppCompatImageButton);
        toolbarTitleTextView = findViewById(R.id.toolbarTitleTextView);
        progressBar = findViewById(R.id.progressBar);
        navigation = findViewById(R.id.nav_view);
    }
    private void inicializarEventos() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        backAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
    private RisponsibleReportsFragment getReportsFragment() {
        if (reportsFragment == null) {
            reportsFragment = new RisponsibleReportsFragment();
            reportsFragment.setPatient(patient);
            reportsFragment.setOnProgressBarChanged(new RisponsibleReportsFragment.OnProgressBarChanged() {
                @Override
                public void OnProgressBarVisible() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void OnProgressBarHide() {
                    progressBar.setVisibility(View.GONE);
                }
            });
            reportsFragment.setOnStepHistoryClicked(new RisponsibleReportsFragment.OnStepHistoryClickedListener() {
                @Override
                public void OnStepHistoryClicked() {
                    mostrarStepsReportActivity();
                }
            });
            reportsFragment.setOnDistanceHistoryClicked(new RisponsibleReportsFragment.OnDistanceHistoryClickedListener() {
                @Override
                public void OnDistanceHistoryClicked() {
                    mostrarDistanceReportActivity();
                }
            });
            reportsFragment.setOnCaloriesHistoryClicked(new RisponsibleReportsFragment.OnCaloriesHistoryClickedListener() {
                @Override
                public void OnCaloriesHistoryClicked() {
                    mostrarCaloriesReportActivity();
                }
            });
            reportsFragment.setOnHeartbeatHistoryClicked(new RisponsibleReportsFragment.OnHeartbeatHistoryClickedListener() {
                @Override
                public void OnHeartbeatHistoryClicked() {
                    mostrarHeartBeatReportActivity();
                }
            });
            reportsFragment.setOnSleepHistoryClicked(new RisponsibleReportsFragment.OnSleepHistoryClickedListener() {
                @Override
                public void OnSleepHistoryClicked() {
                    mostrarSleepReportActivity();
                }
            });
            reportsFragment.setOnWeightHistoryClicked(new RisponsibleReportsFragment.OnWeightHistoryClickedListener() {
                @Override
                public void OnWeightHistoryClicked() {
                    mostrarWeightReportActivity();
                }
            });
            reportsFragment.setOnOximetryClicked(new RisponsibleReportsFragment.OnOximetryClickedListener() {
                @Override
                public void OnOximetryClicked() {
                    mostrarOximetryReportActivity();
                }
            });
        }

        toolbarTitleTextView.setText("Hoy");
        if (patient != null)
            toolbarTitleTextView.setText(patient.getFullName());

        FRAGMENT_TYPE = RisponsibleReportsFragment.FRAGMENT_TYPE_REPORTS;
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

        FRAGMENT_TYPE = ProfileFragment.FRAGMENT_TYPE_PROFILE;
        return profileFragment;
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

    private void mostrarStepsReportActivity() {
        Intent intent = new Intent(this, StepsReportActivity.class);
        intent.putExtra("idUser", patient.getId());
        startActivityForResult(intent, StepsReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarDistanceReportActivity() {
        Intent intent = new Intent(this, DistanceReportActivity.class);
        intent.putExtra("idUser", patient.getId());
        startActivityForResult(intent, DistanceReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarCaloriesReportActivity() {
        Intent intent = new Intent(this, CaloriesReportActivity.class);
        intent.putExtra("idUser", patient.getId());
        startActivityForResult(intent, CaloriesReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarHeartBeatReportActivity() {
        Intent intent = new Intent(this, HeartBeatReportActivity.class);
        intent.putExtra("idUser", patient.getId());
        startActivityForResult(intent, HeartBeatReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarSleepReportActivity() {
        Intent intent = new Intent(this, SleepReportActivity.class);
        intent.putExtra("idUser", patient.getId());
        startActivityForResult(intent, SleepReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarWeightReportActivity() {
        Intent intent = new Intent(this, WeightReportActivity.class);
        intent.putExtra("idUser", patient.getId());
        startActivityForResult(intent, WeightReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    private void mostrarOximetryReportActivity() {
        Intent intent = new Intent(this, OximetryReportActivity.class);
        intent.putExtra("idUser", patient.getId());
        startActivityForResult(intent, OximetryReportActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
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
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);
    }

    @Override
    public void onBackPressed() {
        if (FRAGMENT_TYPE != RisponsibleReportsFragment.FRAGMENT_TYPE_REPORTS) {
            navigation.setSelectedItemId(R.id.navigation_reportes);
            navigateAccordingTo(R.id.navigation_reportes);
        }
        else super.onBackPressed();
    }
}