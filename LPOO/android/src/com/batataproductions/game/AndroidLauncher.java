package com.batataproductions.game;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.batataproductions.game.controller.GameController;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AndroidLauncher extends AndroidApplication implements PlayServices{
	private String userName;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create the client used to sign in to Google services.
		mGoogleSignInClient = GoogleSignIn.getClient(this,
				new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

		signIn();
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new DeathmatchMania(), config);
	}

	// Client used to sign in with Google APIs
	private GoogleSignInClient mGoogleSignInClient;

	private PlayersClient mPlayersClient;

	// request codes we use when invoking an external activity
	private static final int RC_UNUSED = 5001;
	private static final int RC_SIGN_IN = 9001;


	private boolean isSignedIn() {
		return GoogleSignIn.getLastSignedInAccount(this) != null;
	}

	private void signInSilently() {
		GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
				GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
		signInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							// The signed in account is stored in the task's result.
							GoogleSignInAccount signedInAccount = task.getResult();
						} else {
							// Player will need to sign-in explicitly using via UI
						}
					}
				});
	}

	private void startSignInIntent() {
		startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Since the state of the signed in user can change when the activity is not active
		// it is recommended to try and sign in silently from when the app resumes.
		signInSilently();
	}

	@Override
	public void signIn() {
		startSignInIntent();
	}

	@Override
	public void signOut() {

		if (!isSignedIn()) {
			return;
		}

		mGoogleSignInClient.signOut().addOnCompleteListener(this,
				new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						boolean successful = task.isSuccessful();
						onDisconnected();
					}
				});
	}

	public String getName() {

		// Set the greeting appropriately on main menu
		mPlayersClient.getCurrentPlayer()
				.addOnCompleteListener(new OnCompleteListener<Player>() {
					@Override
					public void onComplete(@NonNull Task<Player> task) {
						String displayName;
						if (task.isSuccessful()) {
							displayName = task.getResult().getDisplayName();
						} else {
							Exception e = task.getException();
							displayName = "???";
						}
						userName = displayName;
					}
				});
		return userName;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task =
					GoogleSignIn.getSignedInAccountFromIntent(intent);

			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				onConnected(account);
			} catch (ApiException apiException) {
				String message = apiException.getMessage();
				if (message == null || message.isEmpty()) {
					message = "Other error.";
				}

				onDisconnected();

				new AlertDialog.Builder(this)
						.setMessage(message)
						.setNeutralButton(android.R.string.ok, null)
						.show();
			}
		}
	}

	private void onConnected(GoogleSignInAccount googleSignInAccount) {
		mPlayersClient = Games.getPlayersClient(this, googleSignInAccount);


		// Set the greeting appropriately on main menu
		mPlayersClient.getCurrentPlayer()
				.addOnCompleteListener(new OnCompleteListener<Player>() {
					@Override
					public void onComplete(@NonNull Task<Player> task) {
						String displayName;
						if (task.isSuccessful()) {
							displayName = task.getResult().getDisplayName();
						} else {
							Exception e = task.getException();
							displayName = "???";
						}
					}
				});

	}

	private void onDisconnected() {

		mPlayersClient = null;
	}

}
