package com.batataproductions.game.controller.topscores;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Array;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class TopScores implements Net.HttpResponseListener {

	/**
	 * The singleton instance of the game model
	 */
	private static TopScores instance;

	ArrayList<Score> topScores;

	boolean newHighScore = false;

	/**
	 * Returns a singleton instance of the TopScores class
	 *
	 * @return the singleton instance
	 */
	public static TopScores getInstance() {
		if (instance == null)
			instance = new TopScores();
		return instance;
	}

	private TopScores()
	{
		topScores = new ArrayList<Score>();
		this.fetchHighScores();
	}

	private void fetchHighScores()
	{
		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("http://ptfun.net/dmania/high_scores.php?action=FETCH").build();
		Gdx.net.sendHttpRequest (httpRequest, this);
	}

	private void publicHighScore(String name, int score)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("action", "INSERT");
		parameters.put("name", name);
		parameters.put("score", "" + score);

		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("http://ptfun.net/dmania/high_scores.php").build();
		httpRequest.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		Gdx.net.sendHttpRequest (httpRequest, this);
	}

	@Override
	public void handleHttpResponse(Net.HttpResponse httpResponse) {


		String result = httpResponse.getResultAsString();

		if (result.length() < 5) // Invalid response.
		{
			fetchHighScores();
			return;
		}

		topScores.clear();
		String[] lines = result.split("<br>");

		for(int i = 0; i < lines.length; i++){
			String[] parts = lines[i].split(":");
			String name = parts[0];
			Integer score = Integer.parseInt(parts[1]);
			topScores.add( new Score(name, score) );
		}
	}

	public ArrayList<Score> getTopScores()
	{
		return topScores;
	}


	public boolean isLoaded()
	{
		if(topScores.isEmpty())
			return false;
		else
			return true;
	}

	public boolean isNewHighScore()
	{
		return newHighScore;
	}

	public void resetNewHighScore()
	{
		newHighScore = false;
	}

	public void newScore(int newScore)
	{
		Score toRemove = null;
		for(Score score: topScores)
		{
			if(score.getScore() < newScore)
			{
				toRemove = score;
				break;
			}
		}
		if(toRemove != null)
		{
			topScores.remove(toRemove);
			if (Gdx.app.getType() != Application.ApplicationType.Android)
				this.publicHighScore( System.getProperty("user.name"), newScore);
			else
				this.publicHighScore("ANDROID PLAYER", newScore);
			newHighScore = true;
			fetchHighScores();
		}
	}

	@Override
	public void failed(Throwable t) {
		fetchHighScores();
	}

	@Override
	public void cancelled() {
		fetchHighScores();
	}

	public class Score{
		private String name;
		private int score;

		Score(String name, int score)
		{
			this.name = name;
			this.score = score;
		}

		public String getName()
		{
			return name;
		}

		public int getScore()
		{
			return score;
		}
	}
}
