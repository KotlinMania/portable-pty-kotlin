# port-lint Proposed Changes

**Generated:** 2026-08-25
**Source:** tmp/portable-pty/src
**Target:** src/commonMain/kotlin/io/github/kotlinmania/portablepty

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/portablepty/CommandBuilder.kt` | `// port-lint: source tmp/portable-pty/src/cmdbuilder.rs` | `// port-lint: source cmdbuilder.rs` | `cmdbuilder.rs` | `port-lint provenance header matched only after fallback normalization: 'tmp/portable-pty/src/cmdbuilder.rs' vs expected 'cmdbuilder.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/portablepty/Pty.kt` | `// port-lint: source tmp/portable-pty/src/lib.rs` | `// port-lint: source lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'tmp/portable-pty/src/lib.rs' vs expected 'lib.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/portablepty/PtyTest.kt` | `// port-lint: tests tmp/portable-pty/src/lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:tmp/portable-pty/src/lib.rs' vs expected 'lib.rs'` |
