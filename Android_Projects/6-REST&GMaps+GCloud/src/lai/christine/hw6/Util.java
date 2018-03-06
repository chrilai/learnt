package lai.christine.hw6;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Util {
	
	public static List<UFOPosition> getUFOList(JSONArray jsonArray) throws JSONException {
		List<UFOPosition> ufoList = new ArrayList<UFOPosition>();
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ufoList.add(getUFOPosition( (JSONObject) jsonArray.get(i) ));
			}
		} 
		return ufoList;
	}
	
	public static UFOPosition getUFOPosition(JSONObject jsonObject) {
		return new UFOPosition(
				jsonObject.optInt("ship"),
				jsonObject.optDouble("lat"),
				jsonObject.optDouble("lon")
			);
	}
	
	public static String getJSONArray(List<UFOPosition> ufoList) throws JSONException {
		JSONStringer stringer = new JSONStringer().array();
		for (UFOPosition ufoPosition : ufoList) {
        	stringer.object()
	            .key("ship").value(ufoPosition.getShipNumber())
	            .key("lat").value(ufoPosition.getLat())
	            .key("lon").value(ufoPosition.getLon())
			.endObject();
		}
		return stringer.endArray().toString();
	}
	
	public static String getJSONObject(UFOPosition ufoPosition) throws JSONException {
        JSONStringer stringer = new JSONStringer().object()
            .key("ship").value(ufoPosition.getShipNumber())
            .key("lat").value(ufoPosition.getLat())
            .key("lon").value(ufoPosition.getLon());
		return stringer.endObject().toString();
	}	
}