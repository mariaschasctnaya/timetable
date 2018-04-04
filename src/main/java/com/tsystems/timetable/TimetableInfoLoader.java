package com.tsystems.timetable;

import com.tsystems.timetable.dto.Timetable;
import com.tsystems.timetable.dto.Train;
import com.tsystems.timetable.dto.TrainTimetable;
import com.tsystems.timetable.producer.Property;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import java.util.*;


@Singleton
@Startup
public class TimetableInfoLoader {

    private Map<String, Collection<TrainTimetable>> stationsTimetableStorage;

    @Inject
    @Property("train-service.url")
    private String trainServiceUrl;

    @PostConstruct
    public void init() {
        fillStationTimetableStorage();
    }

    public Map<String, Collection<TrainTimetable>> getStationsTimetableStorage() {
        return stationsTimetableStorage;
    }


    private void fillStationTimetableStorage() {
        Client client = ClientBuilder.newClient();
        List<Train> stationTrains = client.target(trainServiceUrl)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Train>>() {
                });
        stationsTimetableStorage = new HashMap<>();
        stationTrains.forEach(this::toTrainTimetable);
    }

    private void toTrainTimetable(Train train) {
        train.getStationSchedules().forEach((k, v) -> putTrainTimetable(train.getNumber(), k, v));
    }

    private void putTrainTimetable(String trainNumber, String station, Timetable timetable) {
        stationsTimetableStorage.putIfAbsent(station, new HashSet<>());
        TrainTimetable trainTimetable = new TrainTimetable();
        trainTimetable.setId(trainNumber);
        trainTimetable.setArrivedTime(timetable.getArrive());
        trainTimetable.setDepartureTime(timetable.getDeparture());
        trainTimetable.setStatus(timetable.getStatus());
        stationsTimetableStorage.get(station).add(trainTimetable);
    }
}

