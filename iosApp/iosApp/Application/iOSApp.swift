import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        //KoinHelperKt.initialize()
        //KMMHelper.shared.initialize()
        
    }
    
    
	var body: some Scene {
		WindowGroup {
			//ContentView()
            LoginView()
        }
	}
}
