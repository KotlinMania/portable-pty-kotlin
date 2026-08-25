# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 2/8 (25.0%)
- **Function parity:** 22/119 matched (target 32) — 18.5%
- **Class/type parity:** 10/34 matched (target 11) — 29.4%
- **Combined symbol parity:** 32/153 matched (target 43) — 20.9%
- **Average inline-code cosine:** 0.33 (function body across 1 matched files)
- **Average documentation cosine:** 0.17 (doc text across 1 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 2 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. cmdbuilder

- **Target:** `portablepty.CommandBuilder [PROVENANCE-FALLBACK]`
- **Similarity:** 0.33
- **Dependents:** 0
- **Priority Score:** 193906.7
- **Functions:** 18/37 matched (target 20)
- **Missing functions:** `get_shell`, `get_base_env`, `reg_value_to_string`, `get_argv_mut`, `iter_extra_env_as_str`, `iter_full_env_as_str`, `umask`, `resolve_path`, `search_path`, `as_command`, `get_home_dir`, `current_directory`, `environment_block`, `cmdline`, `append_quoted`, `is_cwd_relative_path`, `test_cwd_relative`, `test_env`, `test_env_case_insensitive_override`
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Tests:** 0/3 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tmp/portable-pty/src/cmdbuilder.rs` vs expected `cmdbuilder.rs`
- **Proposed provenance header:** `// port-lint: source cmdbuilder.rs` (current: `// port-lint: source tmp/portable-pty/src/cmdbuilder.rs`)
- **Lint issues:** 1

### 2. lib

- **Target:** `portablepty.Pty [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 152710.0
- **Functions:** 4/16 matched (target 12)
- **Missing functions:** `default`, `get_termios`, `signal`, `from`, `fmt`, `try_wait`, `wait`, `process_id`, `as_raw_handle`, `kill`, `clone_killer`, `native_pty_system`
- **Types:** 8/11 matched (target 9)
- **Missing types:** `ProcessSignaller`, `RawDup`, `NativePtySystem`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tmp/portable-pty/src/lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:tmp/portable-pty/src/lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source tmp/portable-pty/src/lib.rs`)
- **Proposed provenance header:** `// port-lint: tests lib.rs` (current: `// port-lint: tests tmp/portable-pty/src/lib.rs`)
- **Lint issues:** 2

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `win.mod` | `win.Mod` | 0 | `win/mod.rs` | `win/Mod.kt` |

