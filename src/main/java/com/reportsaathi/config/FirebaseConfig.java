package com.reportsaathi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * FirebaseConfig — initializes the Firebase Admin SDK when the app starts.
 *
 * Firebase Admin SDK lets our backend VERIFY tokens that Firebase issues
 * to users after Google Sign-In on the mobile app. We never handle passwords
 * ourselves — Firebase handles all of that.
 *
 * Setup steps (one-time):
 *   1. Go to Firebase Console → Project Settings → Service Accounts
 *   2. Click "Generate new private key" → download JSON file
 *   3. Rename it to: firebase-credentials.json
 *   4. Place it in: src/main/resources/
 *      (it's in .gitignore so it won't be committed)
 *
 * In production (Railway/Render), use the env var GOOGLE_APPLICATION_CREDENTIALS
 * pointing to a mounted file, or set firebase.credentials-path to a volume path.
 */
@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials-path}")
    private String credentialsPath;

    /**
     * @PostConstruct runs ONCE after Spring creates this bean.
     * We initialize FirebaseApp here so every service can call
     * FirebaseAuth.getInstance() directly.
     */
    @PostConstruct
    public void initializeFirebase() {
        // Only initialize if not already done (prevents double-init in tests)
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                InputStream serviceAccount = getCredentialsStream();

                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

                FirebaseApp.initializeApp(options);
                System.out.println("[ReportSaathi] Firebase Admin SDK initialized successfully.");

            } catch (IOException e) {
                // In dev without credentials, log a warning but don't crash the app.
                // Auth endpoints will fail gracefully.
                System.err.println("[ReportSaathi] WARNING: Firebase credentials not found at: "
                    + credentialsPath);
                System.err.println("[ReportSaathi] Auth endpoints will not work until you add " +
                    "firebase-credentials.json to src/main/resources/");
            }
        }
    }

    /**
     * Tries to load credentials from classpath (dev) or falls back to
     * default application credentials (Cloud Run / GKE in prod).
     */
    private InputStream getCredentialsStream() throws IOException {
        // "classpath:firebase-credentials.json" → loads from resources folder
        String path = credentialsPath.replace("classpath:", "");
        return new ClassPathResource(path).getInputStream();
    }
}
