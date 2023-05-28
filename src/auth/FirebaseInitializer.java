package auth;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

// Map<String,Object>data=new HashMap<>();data.put("name","John Doe");data.put("age",30);data.put("city","New York");FirebaseInitializer.addDocument("673","Record",data);

// FirebaseInitializer.readDocument("673","Record");

public class FirebaseInitializer {

	public static void initialize() {
		try {
			FileInputStream refreshToken = new FileInputStream("src/auth/serviceAccountKey.json");
			FirebaseOptions options = null;
			try {
				options = FirebaseOptions.builder()
						.setCredentials(GoogleCredentials.fromStream(refreshToken))
						.setDatabaseUrl("https://javagame-bbdee-default-rtdb.firebaseio.com")
						.build();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Successful connect to firebase!");
			FirebaseApp.initializeApp(options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Firestore getFirestoreInstance() {
		Firestore db = FirestoreClient.getFirestore();
		return db;
	}

	public static void addDocument(String collectionName, String documentId, Map<String, Object> data) {
		Firestore db = getFirestoreInstance();
		DocumentReference docRef = db.collection(collectionName).document(documentId);
		ApiFuture<WriteResult> result = docRef.set(data);

		try {
			System.out.println("Added document with ID: " + documentId + ", at: " + result.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static List<DocumentSnapshot> readDocumentsByCollectionName(String collectionName) {
		Firestore db = getFirestoreInstance();
		ApiFuture<QuerySnapshot> future = db.collection(collectionName).get();

		List<DocumentSnapshot> documents = new ArrayList<>();

		try {
			QuerySnapshot querySnapshot = future.get();
			for (QueryDocumentSnapshot document : querySnapshot) {
				if (document.exists()) {
					documents.add(document);
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return documents;
	}
}
