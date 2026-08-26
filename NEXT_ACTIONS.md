# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 2/8 (25.0%)
- **Function parity:** 25/119 matched (target 35) — 21.0%
- **Class/type parity:** 10/34 matched (target 12) — 29.4%
- **Combined symbol parity:** 35/153 matched (target 47) — 22.9%
- **Average inline-code cosine:** 0.26 (function body across 2 matched files)
- **Average documentation cosine:** 0.42 (doc text across 2 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 2 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. cmdbuilder

- **Target:** `portablepty.CommandBuilder`
- **Similarity:** 0.37
- **Dependents:** 0
- **Priority Score:** 163906.3
- **Functions:** 21/37 matched (target 23)
- **Missing functions:** `get_shell`, `get_base_env`, `reg_value_to_string`, `get_argv_mut`, `iter_extra_env_as_str`, `iter_full_env_as_str`, `umask`, `resolve_path`, `search_path`, `as_command`, `get_home_dir`, `current_directory`, `environment_block`, `cmdline`, `append_quoted`, `test_env_case_insensitive_override`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 2/3 matched

### 2. lib

- **Target:** `portablepty.Pty`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 152708.5
- **Functions:** 4/16 matched (target 12)
- **Missing functions:** `default`, `get_termios`, `signal`, `from`, `fmt`, `try_wait`, `wait`, `process_id`, `as_raw_handle`, `kill`, `clone_killer`, `native_pty_system`
- **Types:** 8/11 matched (target 9)
- **Missing types:** `ProcessSignaller`, `RawDup`, `NativePtySystem`

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

