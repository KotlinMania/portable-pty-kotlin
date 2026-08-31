# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/8 (100.0%)
- **Function parity:** 119/120 matched (target 191) — 99.2%
- **Class/type parity:** 38/38 matched (target 52) — 100.0%
- **Combined symbol parity:** 157/158 matched (target 243) — 99.4%
- **Average inline-code cosine:** 0.47 (function body across 6 matched files)
- **Average documentation cosine:** 0.31 (doc text across 6 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 7 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. win.psuedocon

- **Target:** `win.Psuedocon [PROVENANCE-FALLBACK]`
- **Similarity:** 0.31
- **Dependents:** 1
- **Priority Score:** 1000706.9
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `portable-pty/src/win/psuedocon.rs` vs expected `win/psuedocon.rs`
- **Proposed provenance header:** `// port-lint: source win/psuedocon.rs` (current: `// port-lint: source portable-pty/src/win/psuedocon.rs`)
- **Lint issues:** 1

### 2. lib

- **Target:** `portablepty.Pty [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 12710.0
- **Functions:** 15/16 matched (target 45)
- **Missing functions:** `signal`
- **Types:** 11/11 matched (target 16)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `portable-pty/src/lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:portable-pty/src/lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source portable-pty/src/lib.rs`)
- **Proposed provenance header:** `// port-lint: tests lib.rs` (current: `// port-lint: tests portable-pty/src/lib.rs`)
- **Lint issues:** 2

### 3. cmdbuilder

- **Target:** `portablepty.CommandBuilder [PROVENANCE-FALLBACK]`
- **Similarity:** 0.61
- **Dependents:** 0
- **Priority Score:** 3903.9
- **Functions:** 37/37 matched (target 41)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 3/3 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `portable-pty/src/cmdbuilder.rs` vs expected `cmdbuilder.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:portable-pty/src/cmdbuilder.rs` vs expected `cmdbuilder.rs`
- **Proposed provenance header:** `// port-lint: source cmdbuilder.rs` (current: `// port-lint: source portable-pty/src/cmdbuilder.rs`)
- **Proposed provenance header:** `// port-lint: tests cmdbuilder.rs` (current: `// port-lint: tests portable-pty/src/cmdbuilder.rs`)
- **Lint issues:** 2

### 4. serial

- **Target:** `portablepty.Serial [PROVENANCE-FALLBACK]`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 3304.7
- **Functions:** 25/25 matched (target 34)
- **Missing functions:** _none_
- **Types:** 8/8 matched (target 12)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `portable-pty/src/serial.rs` vs expected `serial.rs`
- **Proposed provenance header:** `// port-lint: source serial.rs` (current: `// port-lint: source portable-pty/src/serial.rs`)
- **Lint issues:** 1

### 5. unix

- **Target:** `portablepty.Unix [PROVENANCE-FALLBACK]`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 2404.9
- **Functions:** 18/18 matched (target 36)
- **Missing functions:** _none_
- **Types:** 6/6 matched (target 8)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `portable-pty/src/unix.rs` vs expected `unix.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:portable-pty/src/unix.rs` vs expected `unix.rs`
- **Proposed provenance header:** `// port-lint: source unix.rs` (current: `// port-lint: source portable-pty/src/unix.rs`)
- **Proposed provenance header:** `// port-lint: tests unix.rs` (current: `// port-lint: tests portable-pty/src/unix.rs`)
- **Lint issues:** 2

### 6. win.mod

- **Target:** `win.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1310.0
- **Functions:** 9/9 matched (target 14)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `portable-pty/src/win/mod.rs` vs expected `win/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:portable-pty/src/win/mod.rs` vs expected `win/mod.rs`
- **Proposed provenance header:** `// port-lint: source win/mod.rs` (current: `// port-lint: source portable-pty/src/win/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests win/mod.rs` (current: `// port-lint: tests portable-pty/src/win/mod.rs`)
- **Lint issues:** 2

### 7. win.conpty

- **Target:** `win.Conpty [PROVENANCE-FALLBACK]`
- **Similarity:** 0.54
- **Dependents:** 0
- **Priority Score:** 1004.6
- **Functions:** 6/6 matched (target 10)
- **Missing functions:** _none_
- **Types:** 4/4 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `portable-pty/src/win/conpty.rs` vs expected `win/conpty.rs`
- **Proposed provenance header:** `// port-lint: source win/conpty.rs` (current: `// port-lint: source portable-pty/src/win/conpty.rs`)
- **Lint issues:** 1

### 8. win.procthreadattr

- **Target:** `win.Procthreadattr [PROVENANCE-FALLBACK]`
- **Similarity:** 0.34
- **Dependents:** 0
- **Priority Score:** 506.6
- **Functions:** 4/4 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `portable-pty/src/win/procthreadattr.rs` vs expected `win/procthreadattr.rs`
- **Proposed provenance header:** `// port-lint: source win/procthreadattr.rs` (current: `// port-lint: source portable-pty/src/win/procthreadattr.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

