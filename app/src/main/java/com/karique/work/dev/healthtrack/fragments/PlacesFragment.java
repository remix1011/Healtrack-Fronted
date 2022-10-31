package com.karique.work.dev.healthtrack.fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.models.PlaceHistory;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;
import com.libizo.CustomEditText;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlacesFragment extends Fragment {
    public static final int FRAGMENT_TYPE_PLACES = 3;
    public static final String TAG_LAST_POSITION = "POSITION";

    private GoogleMap myGoogleMap;
    private SupportMapFragment supportMapFragment;
    private List<PlaceHistory> placeHistories;

    private SessionManager sessionManager;

    private Patient patient;

    private CardView searchFilterCardView;
    private CustomEditText startDateCustomEditText;
    private CustomEditText endDateCustomEditText;
    private int tipoFechaDesde = 1;
    private int tipoFechaHasta = 2;
    private Date fechaDesde = null;
    private Date fechaHasta = null;

    public PlacesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarControles(view);
        inicializarDatos();
        inicializarEventos();
    }

    private void inicializarControles(View view) {
        searchFilterCardView = view.findViewById(R.id.searchFilterCardView);
        startDateCustomEditText = view.findViewById(R.id.startDateCustomEditText);
        endDateCustomEditText = view.findViewById(R.id.endDateCustomEditText);
    }
    private void inicializarDatos() {
        sessionManager = SessionManager.getInstance(getContext());
        placeHistories = new ArrayList<>();

        //setea las fechas a hoy y hace 7 dias
        fechaHasta = FuncionesFecha.getCurrentDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaHasta);
        cal.add(Calendar.DATE, -7);
        fechaDesde = cal.getTime();
        startDateCustomEditText.setText(FuncionesFecha.formatDateForAPI(fechaDesde));
        endDateCustomEditText.setText(FuncionesFecha.formatDateForAPI(fechaHasta));

        //inicializo mapa
        supportMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.google_map);

        //proceso async
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                myGoogleMap = googleMap;
                setPaddingForGoogleLogo(myGoogleMap);
                animateToLima();
                getPlacesHistory();
            }
        });
    }
    private void inicializarEventos() {
        searchFilterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPlacesHistory();
            }
        });
        startDateCustomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFecha(tipoFechaDesde);
            }
        });
        endDateCustomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFecha(tipoFechaHasta);
            }
        });
    }
    private void seleccionarFecha(final int tipoFecha) {
        DatePickerDialog dpdFecha;
        final Calendar _calendario = Calendar.getInstance();
        _calendario.setTime(tipoFecha == tipoFechaDesde ? fechaDesde : fechaHasta);
        dpdFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                _calendario.set(year, monthOfYear, dayOfMonth);
                if (tipoFecha == tipoFechaDesde) {
                    fechaDesde = _calendario.getTime();
                    startDateCustomEditText.setText(FuncionesFecha.formatDateForAPI(fechaDesde));
                }
                else {
                    fechaHasta = _calendario.getTime();
                    endDateCustomEditText.setText(FuncionesFecha.formatDateForAPI(fechaHasta));
                }
            }
        },
                _calendario.get(Calendar.YEAR),
                _calendario.get(Calendar.MONTH),
                _calendario.get(Calendar.DAY_OF_MONTH)
        );
        dpdFecha.getDatePicker().setMaxDate(new Date().getTime());
        dpdFecha.setButton(DatePickerDialog.BUTTON_POSITIVE, "Listo", dpdFecha);
        dpdFecha.setTitle(tipoFecha == tipoFechaDesde ? "Seleccionar fecha desde" : "Seleccionar fecha hasta");
        dpdFecha.show();
    }
    public void animateToLima() {
        LatLng latLngLima = new LatLng(-12.032647501769164, -77.04065880720056);
        animateToCoordinate(latLngLima);
    }
    public void animateToCoordinate(LatLng latLng) {
        //animacion del zoom del marker
        myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }
    public void getPlacesHistory() {
        onProgressBarChanged.OnProgressBarVisible();
        if (isVisible()) {

            int userId = patient == null ? sessionManager.getiduser() : patient.getId();
            String dayStart = startDateCustomEditText.getText().toString();
            String dayEnd = endDateCustomEditText.getText().toString();
            String url = HealthTrackApi.PLACES_HISTORY_BY_USER_IN_DATES(userId, dayStart, dayEnd);
            AndroidNetworking.get(url)
                    .addHeaders("Content-Type", "application/json")
                    .setPriority(Priority.HIGH)
                    .setTag(getString(R.string.app_name))
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.i(TAG_LAST_POSITION,"Recibe posicion");
                            onProgressBarChanged.OnProgressBarHide();

                            placeHistories.clear();
                            placeHistories.addAll(PlaceHistory.from(response));
                            actualizarHeatMap();
                        }

                        @Override
                        public void onError(ANError anError) {
                            onProgressBarChanged.OnProgressBarHide();
                            Toast.makeText(getContext(), "Error al obtener ubicación: " + anError.getErrorDetail(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    private void actualizarHeatMap() {
        //elimino todos los marcadores
        myGoogleMap.clear();
        if (placeHistories.size() == 0)  {
            Toast.makeText(getContext(), "Sin lugares registrados", Toast.LENGTH_LONG).show();
            return;
        }

        List<WeightedLatLng> weightedLatLngs = new ArrayList<>();
        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < placeHistories.size(); i++) {
            //valido que las coordenadas sean doubles
            if (!isNumeric(placeHistories.get(i).getLat()) || !isNumeric(placeHistories.get(i).getLng()))
                break;

            double lat = Double.parseDouble(placeHistories.get(i).getLat());
            double lng = Double.parseDouble(placeHistories.get(i).getLng());
            LatLng latLng = new LatLng(lat, lng);
            latLngs.add(latLng);

            weightedLatLngs.add(new WeightedLatLng(
                    latLng,
                    50
            ));
        }

        HeatmapTileProvider heatMapProvider = new HeatmapTileProvider.Builder()
                .weightedData(weightedLatLngs) // load our weighted data
                .radius(50) // optional, in pixels, can be anything between 20 and 50
                .maxIntensity(100)
                .build();
        myGoogleMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatMapProvider));
        animateCameraToHeats(latLngs);
        //LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //for (int i = 0; i < placeHistories.size(); i++) {
        //    //valido que las coordenadas sean doubles
        //    if (!isNumeric(placeHistories.get(i).getLat()) || !isNumeric(placeHistories.get(i).getLng()))
        //        break;
//
        //    //incializo las opciones de los marcadores
        //    MarkerOptions markerOptions = new MarkerOptions();
        //    double lat = Double.parseDouble(placeHistories.get(i).getLat());
        //    double lng = Double.parseDouble(placeHistories.get(i).getLng());
//
        //    LatLng latLng = new LatLng(lat, lng);
        //    markerOptions.position(latLng);
//
        //    //titulo de los marcadores
        //    markerOptions.title(placeHistories.get(i).getRecordDate());
//
        //    //actualizo el marker al mapa
        //    myCarMarker = myGoogleMap.addMarker(markerOptions);
        //    myCarMarker.showInfoWindow();
//
        //    //animacion del zoom del marker
        //    builder.include(myCarMarker.getPosition());
        //}
//
        //LatLngBounds bounds = builder.build();
        //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
        //myGoogleMap.moveCamera(cu);
        //myGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
        //    @Override
        //    public void onInfoWindowClick(Marker marker) {
        //        showMarkerDetails(marker);
        //    }
        //});
    }
    private void animateCameraToHeats(List<LatLng> latLngs) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < latLngs.size(); i++) {
            builder.include(latLngs.get(i));
        }

        LatLngBounds bounds = builder.build();
        myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
    }
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public void setPaddingForGoogleLogo(GoogleMap map) {
        map.setPadding(dpToPx(16),
                dpToPx(16),
                dpToPx(16),
                dpToPx(16)
        );
    }

    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public interface OnProgressBarChanged {
        void OnProgressBarVisible();
        void OnProgressBarHide();
    }
    private OnProgressBarChanged onProgressBarChanged;
    public void setOnProgressBarChanged(OnProgressBarChanged onProgressBarChanged) {
        this.onProgressBarChanged = onProgressBarChanged;
    }
}