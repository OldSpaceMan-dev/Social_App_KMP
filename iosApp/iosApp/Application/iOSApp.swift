import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        //KoinHelperKt.initialize()
        
        KoinHelperKt.doInitKoin()
        //KMMHelper.shared.initialize()
        //KoinHelper2.initialize()
        
    }
    
    
	var body: some Scene {
		WindowGroup {
			//ContentView()
            LoginView()
        }
	}
}
