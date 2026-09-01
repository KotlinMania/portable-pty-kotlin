# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/12 (66.7%)
- **Function parity:** 104/109 matched (target 146) — 95.4%
- **Class/type parity:** 27/27 matched (target 36) — 100.0%
- **Combined symbol parity:** 131/136 matched (target 182) — 96.3%
- **Average inline-code cosine:** 0.47 (function body across 6 matched files)
- **Average documentation cosine:** 0.31 (doc text across 6 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 7 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. win.psuedocon

- **Target:** `win.Psuedocon`
- **Similarity:** 0.31
- **Dependents:** 1
- **Priority Score:** 1000706.9
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 2. portable-pty.cmdbuilder

- **Target:** `portablepty.CommandBuilder`
- **Similarity:** 0.61
- **Dependents:** 0
- **Priority Score:** 3903.9
- **Functions:** 37/37 matched (target 41)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 3/3 matched

### 3. portable-pty.serial

- **Target:** `portablepty.Serial`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 3304.7
- **Functions:** 25/25 matched (target 34)
- **Missing functions:** _none_
- **Types:** 8/8 matched (target 12)
- **Missing types:** _none_

### 4. portable-pty.unix

- **Target:** `portablepty.Unix`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 2404.9
- **Functions:** 18/18 matched (target 36)
- **Missing functions:** _none_
- **Types:** 6/6 matched (target 8)
- **Missing types:** _none_

### 5. win.mod

- **Target:** `win.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1310.0
- **Functions:** 9/9 matched (target 14)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 5)
- **Missing types:** _none_

### 6. win.conpty

- **Target:** `win.Conpty`
- **Similarity:** 0.54
- **Dependents:** 0
- **Priority Score:** 1004.6
- **Functions:** 6/6 matched (target 10)
- **Missing functions:** _none_
- **Types:** 4/4 matched
- **Missing types:** _none_

### 7. win.procthreadattr

- **Target:** `win.Procthreadattr`
- **Similarity:** 0.34
- **Dependents:** 0
- **Priority Score:** 506.6
- **Functions:** 4/4 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

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

### Matched

| Source | Target | Path |
|--------|--------|------|
| `portable-pty.lib` | `portablepty.Pty` | `portable-pty/src/lib` |

