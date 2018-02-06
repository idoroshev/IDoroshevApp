package com.yandex.android.idoroshevapp.settings;

import com.yandex.android.idoroshevapp.data.AppInfo;

import java.util.Comparator;
 
public class AppComparator {
    private static final String WITHOUT_SORT = "0";
    private static final String ALPHABETICAL = "1";
    private static final String REVERSE_ALPHABETICAL = "2";
    private static final String INSTALLATION_DATE = "3";
    private static final String BY_FREQUENCY = "4";

    public static final String DEFAULT = WITHOUT_SORT;

    public static Comparator<AppInfo> getMethod(String code) {
        switch (code) {
            case AppComparator.WITHOUT_SORT:
                return new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo o1, AppInfo o2) {
                        return 0;
                    }
                };

            case AppComparator.ALPHABETICAL:
                return new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo o1, AppInfo o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                };

            case AppComparator.REVERSE_ALPHABETICAL:
                return new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo o1, AppInfo o2) {
                        return o2.getName().compareToIgnoreCase(o1.getName());
                    }
                };

            case AppComparator.INSTALLATION_DATE:
                return new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo o1, AppInfo o2) {
                        return o2.getUpdateTime().compareTo(o1.getUpdateTime());
                    }
                };

            case AppComparator.BY_FREQUENCY:
                return new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo o1, AppInfo o2) {
                        return o2.getLaunched().compareTo(o1.getLaunched());
                    }
                };

            default:
                return getMethod(AppComparator.DEFAULT);
        }
    }
}
