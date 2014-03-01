package org.codefirex.cfxweather;

import org.codefirex.utils.WeatherInfo;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

public class HttpService extends IntentService {
	private static final String TAG = "WeatherService";
	static final String RESULT_TAG = "weather_service_result";
	static final String RESULT_CODE_TAG = "result_code_tag";
	static final int RESULT_CODE = 2001;
	static final int RESULT_SUCCEED = 2002;
	static final int RESULT_FAIL = 2003;

	private YahooWeatherUtils yahooWeatherUtils = YahooWeatherUtils
			.getInstance();

	public HttpService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		if (WeatherService.LOCATION_ACQUIRED_ACTION.equals(action)) {
			Location location = (Location) intent
					.getParcelableExtra(WeatherService.LOCATION_EXTRA);
			if (location != null) {
				String lat = String.valueOf(location.getLatitude());
				String lon = String.valueOf(location.getLongitude());
				WeatherInfo weatherInfo = yahooWeatherUtils.queryYahooWeather(
						this, lat, lon);
				weatherInfo.setCurrentScale(WeatherPrefs.getDegreeType(this));
				WeatherPrefs.setPrefsFromInfo(this, weatherInfo);
				ResultReceiver rec = intent.getParcelableExtra(RESULT_TAG);
				if (rec != null) {
					Bundle b = new Bundle();
					b.putInt(RESULT_CODE_TAG, RESULT_SUCCEED);
					rec.send(RESULT_CODE, b);
				}
			}
		}
	}
}
