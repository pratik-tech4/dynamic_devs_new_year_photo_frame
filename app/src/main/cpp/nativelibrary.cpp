#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_newyearphotoframes_newyeargreetings_photoeditor_happynewyear_ui_MyApplication_apiUrlFromJNI(JNIEnv* env,jobject /* this */) {
std::string API_URL = "https://babyframeshub.3ftechnolabs.com/";
return env->NewStringUTF(API_URL.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_newyearphotoframes_newyeargreetings_photoeditor_happynewyear_ui_MyApplication_authTokenFromJNI(JNIEnv* env,jobject /* this */) {
    std::string AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9fe5re84gf56gre48d1re4dsg15er48dgf1re56re21reg65";
    return env->NewStringUTF(AUTH_TOKEN.c_str());
}
