# port-lint Proposed Changes

**Generated:** 2026-08-31
**Source:** tmp/portable-pty/src
**Target:** src/commonMain/kotlin/io/github/kotlinmania/portablepty

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/portablepty/win/Psuedocon.kt` | `// port-lint: source portable-pty/src/win/psuedocon.rs` | `// port-lint: source win/psuedocon.rs` | `win/psuedocon.rs` | `port-lint provenance header matched only after fallback normalization: 'portable-pty/src/win/psuedocon.rs' vs expected 'win/psuedocon.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/portablepty/Pty.kt` | `// port-lint: source portable-pty/src/lib.rs` | `// port-lint: source lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'portable-pty/src/lib.rs' vs expected 'lib.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/portablepty/PtyTest.kt` | `// port-lint: tests portable-pty/src/lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:portable-pty/src/lib.rs' vs expected 'lib.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/portablepty/CommandBuilder.kt` | `// port-lint: source portable-pty/src/cmdbuilder.rs` | `// port-lint: source cmdbuilder.rs` | `cmdbuilder.rs` | `port-lint provenance header matched only after fallback normalization: 'portable-pty/src/cmdbuilder.rs' vs expected 'cmdbuilder.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/portablepty/CommandBuilderTest.kt` | `// port-lint: tests portable-pty/src/cmdbuilder.rs` | `// port-lint: tests cmdbuilder.rs` | `cmdbuilder.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:portable-pty/src/cmdbuilder.rs' vs expected 'cmdbuilder.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/portablepty/Serial.kt` | `// port-lint: source portable-pty/src/serial.rs` | `// port-lint: source serial.rs` | `serial.rs` | `port-lint provenance header matched only after fallback normalization: 'portable-pty/src/serial.rs' vs expected 'serial.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/portablepty/Unix.kt` | `// port-lint: source portable-pty/src/unix.rs` | `// port-lint: source unix.rs` | `unix.rs` | `port-lint provenance header matched only after fallback normalization: 'portable-pty/src/unix.rs' vs expected 'unix.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/portablepty/UnixTest.kt` | `// port-lint: tests portable-pty/src/unix.rs` | `// port-lint: tests unix.rs` | `unix.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:portable-pty/src/unix.rs' vs expected 'unix.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/portablepty/win/Mod.kt` | `// port-lint: source portable-pty/src/win/mod.rs` | `// port-lint: source win/mod.rs` | `win/mod.rs` | `port-lint provenance header matched only after fallback normalization: 'portable-pty/src/win/mod.rs' vs expected 'win/mod.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/portablepty/WinTest.kt` | `// port-lint: tests portable-pty/src/win/mod.rs` | `// port-lint: tests win/mod.rs` | `win/mod.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:portable-pty/src/win/mod.rs' vs expected 'win/mod.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/portablepty/win/Conpty.kt` | `// port-lint: source portable-pty/src/win/conpty.rs` | `// port-lint: source win/conpty.rs` | `win/conpty.rs` | `port-lint provenance header matched only after fallback normalization: 'portable-pty/src/win/conpty.rs' vs expected 'win/conpty.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/portablepty/win/Procthreadattr.kt` | `// port-lint: source portable-pty/src/win/procthreadattr.rs` | `// port-lint: source win/procthreadattr.rs` | `win/procthreadattr.rs` | `port-lint provenance header matched only after fallback normalization: 'portable-pty/src/win/procthreadattr.rs' vs expected 'win/procthreadattr.rs'` |
