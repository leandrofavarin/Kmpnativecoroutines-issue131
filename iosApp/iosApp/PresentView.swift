import Combine
import KMPNativeCoroutinesAsync
import KMPNativeCoroutinesCombine
import KMPNativeCoroutinesCore
import SwiftUI
import shared

struct PresentView<Content: View, ModelT: AnyObject>: View {
  @State var model: ModelT
  @State private var models: AnyPublisher<Any, Never> // https://github.com/rickclephas/KMP-NativeCoroutines/issues/90
  private let start: NativeSuspend<KotlinUnit, Error, KotlinUnit>
  private let content: (ModelT) -> Content

  init(
    _ presenter: Presenter<ModelT>,
    content: @escaping (ModelT) -> Content
  ) {
    self.model = presenter.currentModel
    self.models = createPublisher(for: presenter.__modelsFlow)
      .assertNoFailure()
      .eraseToAnyPublisher()
    self.start = presenter.start()
    self.content = content
  }

  var body: some View {
    content(model)
      .onReceive(models) { self.model = $0 as! ModelT }
      .task { await start() }
  }

  @MainActor private func start() async {
    do {
      _ = try await asyncFunction(for: start)
    } catch is CancellationError {
      // Regular cancel signal from Kotlin Coroutines.
    } catch {
      fatalError("Presenter error: \(error)")
    }
  }
}
