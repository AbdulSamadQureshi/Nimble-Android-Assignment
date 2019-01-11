# Nimble-Android-Assignment


Other used components:
- MVVM (Android architecture components)
- Kotlin
- DataBinding
- ViewHolderPattern
- Authenticator to refresh the expired token (TokenAuthenticator)
- Vertical ViewPager with pagination (page size 5)
- Dagger(v-2.9) is used, but not to its full strength
- Retrofit
- Constraint Layout


Things that should have added:
- FastLane/Bitrise
- Crashlytics
- Fabric
- SonarCube
- Proguard


Notes:
- Could not move to AndroidX just for verticalViewPager library, its old and could not get migrated to AndroidX
- Test coverage is not added (Expresso for UI, jUnit for unitTests)
