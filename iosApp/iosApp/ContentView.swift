import SwiftUI
import shared

struct ContentView: View {
  let intPresenter: IntPresenter = IntPresenter()
  
  var body: some View {
    NavigationView {

      NavigationLink {
        PresentView(intPresenter) { model in
          IntPresenterView(model: Int(model))
        }
      } label: {
        Text("Click")
      }

    }
  }
}

struct ContentView_Previews: PreviewProvider {
  static var previews: some View {
    ContentView()
  }
}
