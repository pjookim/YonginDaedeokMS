package com.pebble.daedeokms.settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;

import uk.me.lewisdeane.ldialogs.CustomDialog;
import com.pebble.daedeokms.R;
import com.pebble.daedeokms.autoupdate.updateAlarm;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(R.id.container, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.settings);

            setOnPreferenceClick(findPreference("infoAutoUpdate"));
            setOnPreferenceClick(findPreference("openSource"));
            setOnPreferenceClick(findPreference("ChangeLog"));
            setOnPreferenceClick(findPreference("Developer"));
            setOnPreferenceChange(findPreference("autoBapUpdate"));
            setOnPreferenceChange(findPreference("updateLife"));

            try {
                PackageManager packageManager = getActivity().getPackageManager();
                PackageInfo info = packageManager.getPackageInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
                findPreference("appVersion").setSummary(info.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void setOnPreferenceClick(Preference mPreference) {
            mPreference.setOnPreferenceClickListener(onPreferenceClickListener);
        }

        private Preference.OnPreferenceClickListener onPreferenceClickListener = new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                String getKey = preference.getKey();

                if ("openSource".equals(getKey)) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity(), R.string.license_title, android.R.string.ok);
                    builder.content(getString(R.string.license_msg));
                    CustomDialog customDialog = builder.build();
                    customDialog.show();

                } else if ("ChangeLog".equals(getKey)) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity(), R.string.changeLog_title, android.R.string.ok);
                    builder.content(getString(R.string.changeLog_msg));
                    CustomDialog customDialog = builder.build();
                    customDialog.show();

                } else if ("Developer".equals(getKey)) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity(), R.string.developer_title, android.R.string.ok);
                    builder.content(getString(R.string.developer_msg));
                    CustomDialog customDialog = builder.build();
                    customDialog.show();

                } else if ("infoAutoUpdate".equals(getKey)) {
                    showNotifi();
                }
                return true;
            }
        };

        private void setOnPreferenceChange(Preference mPreference) {
            mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);

            if (mPreference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) mPreference;
                int index = listPreference.findIndexOfValue(listPreference.getValue());
                mPreference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            } else if (mPreference instanceof EditTextPreference) {
                String values = ((EditTextPreference) mPreference).getText();
                if (values == null) values = "";
                onPreferenceChangeListener.onPreferenceChange(mPreference, values);
            }
        }

        private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();

                if (preference instanceof EditTextPreference) {
                    preference.setSummary(stringValue);

                } else if (preference instanceof ListPreference) {

                    /**
                     * ListPreference의 경우 stringValue가 entryValues이기 때문에 바로 Summary를
                     * 적용하지 못한다 따라서 설정한 entries에서 String을 로딩하여 적용한다
                     */
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);

                    preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                    updateAlarm updateAlarm = new updateAlarm(getActivity());
                    updateAlarm.cancle();

                    if (index == 0) updateAlarm.autoUpdate();
                    else if (index == 1) updateAlarm.SaturdayUpdate();
                    else if (index == 2) updateAlarm.SundayUpdate();

                } else if (preference instanceof CheckBoxPreference) {
                    com.pebble.daedeokms.tool.Preference mPref = new com.pebble.daedeokms.tool.Preference(getActivity());

                    if (mPref.getBoolean("firstOfAutoUpdate", true)) {
                        mPref.putBoolean("firstOfAutoUpdate", false);
                        showNotifi();
                    }

                    if (!mPref.getBoolean("autoBapUpdate", false) && preference.isEnabled()) {
                        int updateLife = Integer.parseInt(mPref.getString("updateLife", "0"));

                        updateAlarm updateAlarm = new updateAlarm(getActivity());
                        if (updateLife == 1) updateAlarm.autoUpdate();
                        else if (updateLife == 0) updateAlarm.SaturdayUpdate();
                        else if (updateLife == -1) updateAlarm.SundayUpdate();

                    } else {
                        updateAlarm updateAlarm = new updateAlarm(getActivity());
                        updateAlarm.cancle();
                    }
                }
                return true;
            }
        };

        private void showNotifi() {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity(), R.string.info_autoUpdate_title, android.R.string.ok);
            builder.content(getString(R.string.info_autoUpdate_msg));
            CustomDialog customDialog = builder.build();
            customDialog.show();
        }
    }
}
