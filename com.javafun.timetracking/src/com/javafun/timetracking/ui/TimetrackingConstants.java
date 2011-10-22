package com.javafun.timetracking.ui;

public final class TimetrackingConstants {

    public enum Plugins {
        TimeTrackingPlugin("com.javafun.core");

        private String _id;

        public String getId() {
            return _id;
        }

        public void setId(String id) {
            _id = id;
        }

        private Plugins(String id) {
            _id = id;
        }
    }
}
