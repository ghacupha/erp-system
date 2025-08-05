# GitHub Project Board Management for ERP System

## Overview

The ERP System project uses GitHub Projects for tracking all development work across the microservices modernization journey. This document establishes mandatory workflows and practices adapted from proven methodologies to ensure proper project visibility, coordination, and delivery tracking throughout the 4-phase migration strategy.

**Project Context**: Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2  
**Architecture**: Monolithic Spring Boot application transitioning to microservices  
**Development Methodology**: Epic-based with 5 production-readiness gates  

## Current Project Board Structure

### Recommended Project Board Configuration

**Project Board Name**: `ERP System Modernization - Jehoiada Series`

**Status Columns**:
1. **📋 Backlog** - Prioritized work items awaiting assignment
2. **🔄 In Progress** - Active development work
3. **👀 Code Review** - Pull requests under review
4. **🧪 Testing** - Items in testing phase (unit, integration, E2E)
5. **✅ Done** - Completed and merged work

**Custom Fields**:
- **Epic Phase**: Single select (Phase 1, Phase 2, Phase 3, Phase 4)
- **Business Domain**: Single select (Asset Management, Financial Core, Lease Management, Work in Progress, Document Management, Audit Trail, Fiscal Calendar, Supporting Services)
- **Story Points**: Number (1, 2, 3, 5, 8, 13, 21)
- **Production Gate Status**: Single select (Gate 1: Code Quality, Gate 2: Testing, Gate 3: Security, Gate 4: Performance, Gate 5: Operations, All Gates Passed)
- **Priority**: Single select (Critical, High, Medium, Low)
- **Migration Type**: Single select (Service Extraction, Data Migration, Integration, Infrastructure)

## Branch Strategy Integration

The project board integrates with the existing branch naming convention:

```
feature/epic-{phase}-{epic-number}
hotfix/issue-{number}
release/v{major}.{minor}.{patch}
```

**Examples**:
- `feature/epic-1-1` → Epic 1.1: Document Management Service Extraction
- `feature/epic-2-1` → Epic 2.1: Asset Management Service Extraction
- `feature/epic-3-2` → Epic 3.2: Lease Processing Service Extraction

## 🚨 MANDATORY GitHub Project Board Workflow Protocol

**⚠️ CRITICAL**: All work MUST follow this exact workflow to maintain project visibility and ensure production-readiness gate compliance.

### GitHub Project Board Status Workflow

The project board follows this mandatory progression:
**Backlog** → **In Progress** → **Code Review** → **Testing** → **Done**

### 📋 Mandatory Status Update Points

#### 🟡 START OF WORK: Move to "In Progress" AND Assign

**WHEN**: Before starting any implementation work on an epic or user story  
**REQUIREMENTS**:
- Move issue from "Backlog" to "In Progress"
- Assign issue to yourself
- Set Epic Phase and Business Domain fields
- Estimate Story Points
- Create feature branch following naming convention

**HOW**:
```bash
# 1. Create and checkout feature branch
git checkout -b feature/epic-{phase}-{epic-number}

# 2. Update project board status (via GitHub CLI)
gh project item-edit --id <ITEM_ID> --project-id <PROJECT_ID> --field-id <STATUS_FIELD_ID> --single-select-option-id <IN_PROGRESS_ID>

# 3. Assign issue to yourself
gh issue edit <ISSUE_NUMBER> --assignee @me

# 4. Set custom fields
gh project item-edit --id <ITEM_ID> --project-id <PROJECT_ID> --field-id <EPIC_PHASE_FIELD_ID> --single-select-option-id <PHASE_OPTION_ID>
```

#### 🔄 DURING WORK: Maintain "In Progress" Status

**WHEN**: Throughout implementation, local testing, debugging  
**STATUS**: Remains "In Progress" even when code is complete locally  
**IMPORTANT**: Do NOT move to "Code Review" until PR is created

#### 🔗 PULL REQUEST CREATION: Move to "Code Review"

**WHEN**: Implementation complete, all local tests passing, ready for peer review  
**MANDATORY REQUIREMENTS**:
- All 5 production-readiness gates must be validated locally
- PR title format: `Epic {phase}.{number}: <Brief description of implementation>`
- PR body must include `Resolves: #<ISSUE_NUMBER>` for automatic linking
- Move issue to "Code Review" status

**Production-Readiness Gate Validation**:
```bash
# Gate 1: Code Quality
./mvnw checkstyle:check
./mvnw sonar:sonar  # If SonarQube configured

# Gate 2: Testing Excellence
./mvnw test  # Must achieve ≥80% line coverage, ≥70% branch coverage
./mvnw verify  # Integration tests

# Gate 3: Security Compliance
./mvnw org.owasp:dependency-check-maven:check

# Gate 4: Performance Standards
# Run performance tests if applicable

# Gate 5: Operational Readiness
# Verify health checks, logging, monitoring
```

**PR Creation Example**:
```bash
gh pr create --title "Epic 1.1: Document Management Service Core Implementation" \
  --body "$(cat <<'EOF'
## Summary
Complete implementation of document management service core functionality including upload, download, metadata management, and virus scanning integration.

Resolves: #42

## Production-Readiness Gates Status
- [x] Gate 1: Code Quality - Checkstyle passed, SonarQube quality gate passed
- [x] Gate 2: Testing Excellence - 85% line coverage, 72% branch coverage
- [x] Gate 3: Security Compliance - No high/critical vulnerabilities
- [x] Gate 4: Performance Standards - Response times < 500ms
- [x] Gate 5: Operational Readiness - Health checks implemented

## Implementation Details
- Implemented Spring Boot service with REST endpoints
- Added file integrity verification with SHA-512 checksums
- Integrated virus scanning with ClamAV
- Added comprehensive unit and integration tests
- Implemented circuit breaker patterns for external dependencies

## Test Plan
- [x] All acceptance criteria met
- [x] Unit tests passing with adequate coverage
- [x] Integration tests with Testcontainers
- [x] Security scanning completed
- [x] Performance benchmarks met

## Migration Impact
- Part of Phase 1: Supporting Services extraction
- Enables document management independence from monolith
- Reduces monolith complexity by ~5%

🤖 Generated for ERP System Modernization - Jehoiada Series
EOF
)"
```

#### 🧪 TESTING PHASE: Move to "Testing"

**WHEN**: PR approved and merged, deployed to testing environment  
**REQUIREMENTS**:
- All CI/CD checks passing
- Deployed to staging/testing environment
- End-to-end testing initiated
- Integration testing with dependent services

#### ✅ END OF WORK: Move to "Done"

**WHEN**: ONLY after all testing completed successfully and production deployment verified  
**REQUIREMENTS**:
- All 5 production-readiness gates passed
- E2E testing completed successfully
- Production deployment successful (if applicable)
- Documentation updated
- Epic acceptance criteria fully met

## Epic and User Story Templates

### Epic Issue Template

```markdown
# Epic {Phase}.{Number}: {Epic Title}

## Epic Overview
**Business Value**: {Clear statement of business value}
**Duration**: {Estimated duration}
**Story Points**: {Total story points}
**Phase**: {Migration phase number}
**Business Domain**: {Primary business domain}

## Success Criteria
- [ ] {High-level success criterion 1}
- [ ] {High-level success criterion 2}
- [ ] {High-level success criterion 3}

## User Stories
- [ ] Story {Phase}.{Epic}.1: {User story title} - {Story points} points
- [ ] Story {Phase}.{Epic}.2: {User story title} - {Story points} points
- [ ] Story {Phase}.{Epic}.3: {User story title} - {Story points} points

## Production-Readiness Gates
- [ ] Gate 1: Code Quality ✅
- [ ] Gate 2: Testing Excellence ✅
- [ ] Gate 3: Security Compliance ✅
- [ ] Gate 4: Performance Standards ✅
- [ ] Gate 5: Operational Readiness ✅

## Dependencies
- **Depends on**: {List of dependent epics/stories}
- **Blocks**: {List of blocked epics/stories}

## Technical Considerations
- **Architecture Impact**: {Description of architectural changes}
- **Data Migration**: {Data migration requirements}
- **Integration Points**: {External service integrations}
- **Rollback Strategy**: {Rollback procedures}

## Definition of Done
- [ ] All user stories completed and accepted
- [ ] All production-readiness gates passed
- [ ] Service deployed to production
- [ ] Documentation updated
- [ ] Team trained on new functionality
```

### User Story Issue Template

```markdown
# Story {Phase}.{Epic}.{Number}: {Story Title}

## User Story
As a **{User Role}**, I want {Goal} so that {Business Value}.

## Acceptance Criteria
- [ ] {Specific, testable criterion 1}
- [ ] {Specific, testable criterion 2}
- [ ] {Specific, testable criterion 3}

## Technical Tasks
- [ ] {Technical implementation task 1}
- [ ] {Technical implementation task 2}
- [ ] {Technical implementation task 3}

## Production-Readiness Checklist
### Gate 1: Code Quality
- [ ] Checkstyle violations = 0
- [ ] SonarQube quality gate passed
- [ ] Code coverage ≥80% line, ≥70% branch
- [ ] No high/critical security vulnerabilities

### Gate 2: Testing Excellence
- [ ] Unit tests implemented and passing
- [ ] Integration tests implemented and passing
- [ ] End-to-end test scenarios covered
- [ ] Performance baseline established

### Gate 3: Security Compliance
- [ ] Input validation implemented
- [ ] Authentication/authorization verified
- [ ] Security scanning completed
- [ ] No hardcoded secrets

### Gate 4: Performance Standards
- [ ] Response times meet SLA requirements
- [ ] Resource usage within limits
- [ ] Load testing completed (if applicable)

### Gate 5: Operational Readiness
- [ ] Health checks implemented
- [ ] Logging and monitoring configured
- [ ] Documentation updated
- [ ] Deployment procedures verified

## Definition of Done
- [ ] All acceptance criteria met
- [ ] All production-readiness gates passed
- [ ] Code reviewed and approved
- [ ] Tests passing in CI/CD
- [ ] Documentation updated
```

## 🚫 Common Workflow Violations (DO NOT DO)

❌ **Never move to "Done" when code is complete locally**  
❌ **Never skip the "In Progress" status when starting work**  
❌ **Never leave issues in "Backlog" while actively working**  
❌ **Never move to "Done" before all production-readiness gates pass**  
❌ **Never start work without assigning the issue to yourself**  
❌ **Never use inconsistent PR title formats**:
- ❌ `Implement asset service (Epic 2.1)`
- ❌ `Fix issues - Epic 1.2`
- ✅ `Epic 2.1: Asset Management Service Core Implementation`

❌ **Never omit `Resolves: #<NUMBER>` in PR body**  
❌ **Never merge without all 5 production-readiness gates passing**  
❌ **Never skip testing phase for service extractions**

## Integration with Existing Development Methodology

### Production-Readiness Gates Integration

The project board workflow integrates with the existing 5 production-readiness gates:

1. **Gate 1: Code Quality** - Validated before PR creation
2. **Gate 2: Testing Excellence** - Validated during "Testing" phase
3. **Gate 3: Security Compliance** - Validated before PR creation
4. **Gate 4: Performance Standards** - Validated during "Testing" phase
5. **Gate 5: Operational Readiness** - Validated before moving to "Done"

### Epic-Based Development Integration

Each epic from the migration strategy maps to project board items:

**Phase 1 Examples**:
- Epic 1.1: Document Management Service Extraction → 34 story points
- Epic 1.2: Audit Trail Service Extraction → 34 story points
- Epic 1.3: Fiscal Calendar Service Extraction → 21 story points

**Phase 2 Examples**:
- Epic 2.1: Asset Management Service Extraction → 55 story points
- Epic 2.2: Financial Core Service Extraction → 55 story points

## Automation Recommendations

### GitHub Actions Integration

Create `.github/workflows/project-board-automation.yml`:

```yaml
name: Project Board Automation

on:
  issues:
    types: [opened, assigned, closed]
  pull_request:
    types: [opened, closed, merged]

jobs:
  update-project-board:
    runs-on: ubuntu-latest
    steps:
      - name: Update project board on issue assignment
        if: github.event.action == 'assigned'
        run: |
          gh project item-edit --id ${{ github.event.issue.node_id }} \
            --project-id ${{ vars.PROJECT_ID }} \
            --field-id ${{ vars.STATUS_FIELD_ID }} \
            --single-select-option-id ${{ vars.IN_PROGRESS_ID }}
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

      - name: Move to Code Review on PR creation
        if: github.event.action == 'opened' && github.event_name == 'pull_request'
        run: |
          # Extract issue number from PR body
          ISSUE_NUMBER=$(echo "${{ github.event.pull_request.body }}" | grep -o "Resolves: #[0-9]*" | grep -o "[0-9]*")
          if [ ! -z "$ISSUE_NUMBER" ]; then
            gh project item-edit --id $(gh api graphql -f query='query($owner: String!, $repo: String!, $number: Int!) { repository(owner: $owner, name: $repo) { issue(number: $number) { id } } }' -f owner=${{ github.repository_owner }} -f repo=${{ github.event.repository.name }} -F number=$ISSUE_NUMBER --jq '.data.repository.issue.id') \
              --project-id ${{ vars.PROJECT_ID }} \
              --field-id ${{ vars.STATUS_FIELD_ID }} \
              --single-select-option-id ${{ vars.CODE_REVIEW_ID }}
          fi
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

### Required Repository Variables

Set these in repository settings → Secrets and variables → Actions → Variables:

- `PROJECT_ID`: GitHub project ID
- `STATUS_FIELD_ID`: Status field ID
- `IN_PROGRESS_ID`: "In Progress" option ID
- `CODE_REVIEW_ID`: "Code Review" option ID
- `TESTING_ID`: "Testing" option ID
- `DONE_ID`: "Done" option ID

## Migration Phase Tracking

### Phase 1: Supporting Services (Months 1-2)
**Focus**: Extract foundational services  
**Story Points**: 89 points  
**Key Epics**: Document Management, Audit Trail, Fiscal Calendar

### Phase 2: Core Business Services (Months 3-6)
**Focus**: Extract core business logic  
**Story Points**: 144 points  
**Key Epics**: Asset Management, Financial Core, Lease Management

### Phase 3: Processing Services (Months 7-8)
**Focus**: Extract processing and calculation services  
**Story Points**: 89 points  
**Key Epics**: Depreciation Processing, Report Generation

### Phase 4: Optimization & Scale (Months 9-12)
**Focus**: Performance optimization and scaling  
**Story Points**: 55 points  
**Key Epics**: Performance Tuning, Monitoring, Documentation

## Reporting and Metrics

### Weekly Progress Reports

Generate weekly reports using GitHub CLI:

```bash
# Epic completion status
gh project item-list <PROJECT_NUMBER> --owner JuniperaDev --format json | \
  jq '.items[] | select(.fieldValues.status == "Done") | .content.title'

# Story points completed by phase
gh project item-list <PROJECT_NUMBER> --owner JuniperaDev --format json | \
  jq '.items[] | select(.fieldValues.status == "Done") | .fieldValues.storyPoints' | \
  awk '{sum += $1} END {print "Total completed story points:", sum}'

# Production-readiness gate status
gh project item-list <PROJECT_NUMBER> --owner JuniperaDev --format json | \
  jq '.items[] | select(.fieldValues.productionGateStatus == "All Gates Passed") | length'
```

### Success Metrics

Track these key metrics:
- **Epic Completion Rate**: Percentage of epics completed per phase
- **Story Point Velocity**: Average story points completed per sprint
- **Production-Readiness Gate Pass Rate**: Percentage of stories passing all gates
- **Cycle Time**: Average time from "In Progress" to "Done"
- **Defect Rate**: Number of production issues per epic
- **Migration Progress**: Percentage of monolith complexity reduced

## Manual Project Board Access

For manual updates when CLI is not available:

1. **Project Board URL**: `https://github.com/orgs/JuniperaDev/projects/<PROJECT_NUMBER>`
2. **Find the issue card** in the appropriate column
3. **Drag to appropriate status column** following the mandatory workflow
4. **Update custom fields** as needed (Epic Phase, Business Domain, etc.)
5. **Assign issues** to team members

## Best Practices

### For Epic Owners
- Break epics into manageable user stories (≤13 story points each)
- Ensure all dependencies are identified and tracked
- Validate production-readiness gates before declaring epic complete
- Maintain epic documentation throughout development

### For Developers
- Always assign issues to yourself before starting work
- Follow the mandatory status progression
- Validate all production-readiness gates locally before PR creation
- Include comprehensive testing and documentation

### For Project Managers
- Monitor epic progress weekly using automated reports
- Identify and resolve blockers quickly
- Ensure proper resource allocation across phases
- Track migration progress against business objectives

## Future Automation

Once the ERP System modernization is complete, the system will automate:
- ✅ GitHub issue status synchronization
- ✅ Project board updates based on CI/CD pipeline status
- ✅ Automatic assignment of issues to team members
- ✅ Progress tracking and milestone reporting
- ✅ Production-readiness gate validation
- ✅ Epic completion notifications

For now, manual updates ensure proper project visibility and coordination throughout the microservices modernization journey.

---

**Document Version**: 1.0  
**Last Updated**: August 2025  
**Applies to**: ERP System - Jehoiada Series Mark X No 10  
**Reference**: Adapted from [CC-Orchestrator Project Management Practices](https://github.com/altsang/cc-orchestrator/blob/main/GITHUB_PROJECT_MANAGEMENT.md)
