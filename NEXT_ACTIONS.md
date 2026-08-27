# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 3/8 (37.5%)
- **Function parity:** 58/128 matched (target 89) — 45.3%
- **Class/type parity:** 14/38 matched (target 21) — 36.8%
- **Combined symbol parity:** 72/166 matched (target 110) — 43.4%
- **Average inline-code cosine:** 0.41 (function body across 3 matched files)
- **Average documentation cosine:** 0.47 (doc text across 3 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 3 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. serial

- **Target:** `portablepty.Serial`
- **Similarity:** 0.37
- **Dependents:** 0
- **Priority Score:** 163306.3
- **Functions:** 14/25 matched (target 22)
- **Missing functions:** `new`, `fmt`, `as_raw_handle`, `write`, `flush`, `try_clone_reader`, `take_writer`, `process_group_leader`, `as_raw_fd`, `tty_name`, `read`
- **Types:** 3/8 matched (target 7)
- **Missing types:** `Handle`, `Slave`, `Master`, `MasterWriter`, `Reader`
- **Lint issues:** 1

### 2. lib

- **Target:** `portablepty.Pty`
- **Similarity:** 0.30
- **Dependents:** 0
- **Priority Score:** 72707.0
- **Functions:** 11/16 matched (target 30)
- **Missing functions:** `default`, `get_termios`, `signal`, `from`, `fmt`
- **Types:** 9/11 matched
- **Missing types:** `RawDup`, `NativePtySystem`
- **Lint issues:** 1

### 3. cmdbuilder

- **Target:** `portablepty.CommandBuilder`
- **Similarity:** 0.55
- **Dependents:** 0
- **Priority Score:** 43904.5
- **Functions:** 33/37 matched
- **Missing functions:** `get_base_env`, `reg_value_to_string`, `resolve_path`, `as_command`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 3/3 matched

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

