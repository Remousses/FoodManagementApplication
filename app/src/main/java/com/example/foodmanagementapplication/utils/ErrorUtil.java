package com.example.foodmanagementapplication.utils;

import android.speech.SpeechRecognizer;

public class ErrorUtil {

    public ErrorUtil() {
        throw new IllegalStateException("Never instantiate utility classes");
    }

    public static String getErrorText(final int error) {
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO :
                return "Erreur d'enregistrement audio.";
            case SpeechRecognizer.ERROR_CLIENT :
                return "Erreur côté client.";
            case  SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS :
                return "Permissions insuffisantes.";
            case SpeechRecognizer.ERROR_NETWORK :
                return "Network error.";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT :
                return "Erreur réseau.";
            case SpeechRecognizer.ERROR_NO_MATCH :
                return "Pas de correspondance.";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY :
                return "RecognitionService occupé.";
            case SpeechRecognizer.ERROR_SERVER :
                return "Erreur du serveur.";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT :
                return "Aucune entrée vocale.";
            default :
                return "Je n'ai pas compris, veuillez réessayer.";
        }
    }
}
