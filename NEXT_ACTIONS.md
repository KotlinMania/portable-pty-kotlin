# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/8 (100.0%)
- **Function parity:** 120/120 matched (target 192) — 100.0%
- **Class/type parity:** 38/38 matched (target 52) — 100.0%
- **Combined symbol parity:** 158/158 matched (target 244) — 100.0%
- **Average inline-code cosine:** 0.46 (function body across 7 matched files)
- **Average documentation cosine:** 0.37 (doc text across 7 matched files)
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
- **Similarity:** 0.30
- **Dependents:** 1
- **Priority Score:** 1000707.0
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Lint issues:** 2

### 2. cmdbuilder

- **Target:** `portablepty.CommandBuilder`
- **Similarity:** 0.61
- **Dependents:** 0
- **Priority Score:** 3903.9
- **Functions:** 37/37 matched (target 41)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 3/3 matched

### 3. serial

- **Target:** `portablepty.Serial`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 3304.7
- **Functions:** 25/25 matched (target 34)
- **Missing functions:** _none_
- **Types:** 8/8 matched (target 12)
- **Missing types:** _none_
- **Lint issues:** 2

### 4. lib

- **Target:** `portablepty.Pty`
- **Similarity:** 0.38
- **Dependents:** 0
- **Priority Score:** 2706.2
- **Functions:** 16/16 matched (target 46)
- **Missing functions:** _none_
- **Types:** 11/11 matched (target 16)
- **Missing types:** _none_
- **Lint issues:** 1

### 5. unix

- **Target:** `portablepty.Unix`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 2404.9
- **Functions:** 18/18 matched (target 36)
- **Missing functions:** _none_
- **Types:** 6/6 matched (target 8)
- **Missing types:** _none_
- **Lint issues:** 2

### 6. win.mod

- **Target:** `win.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1310.0
- **Functions:** 9/9 matched (target 14)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 5)
- **Missing types:** _none_

### 7. win.conpty

- **Target:** `win.Conpty`
- **Similarity:** 0.54
- **Dependents:** 0
- **Priority Score:** 1004.6
- **Functions:** 6/6 matched (target 10)
- **Missing functions:** _none_
- **Types:** 4/4 matched
- **Missing types:** _none_

### 8. win.procthreadattr

- **Target:** `win.Procthreadattr`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 506.8
- **Functions:** 4/4 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

