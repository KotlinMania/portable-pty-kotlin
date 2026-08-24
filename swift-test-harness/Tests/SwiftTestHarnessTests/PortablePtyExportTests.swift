import Testing
import PortablePty

@Suite("PortablePty Export Smoke Tests")
struct PortablePtyExportTests {
    @Test("Swift module loads cleanly")
    func testSwiftModuleLoads() throws {
        #expect(true)
    }
}
