# Angular Project Setup Commands

This document contains all the Angular CLI commands used to set up the ui-angular micro-frontend project.

## Project Initialization

### 1. Create Angular Workspace
```bash
# Create a new Angular workspace without a default application
ng new ui-angular --create-application=false
```

### 2. Generate Shell Application
```bash
# Navigate to the ui-angular directory
cd ui-angular

# Generate the main shell application
ng generate application shell --prefix app-shell
```

### 3. Generate Products Application
```bash
# Generate the products micro-frontend application
ng generate application products --prefix app-products
```

## Adding Angular Material

### 4. Add Angular Material to Shell Project
```bash
# Add Angular Material to the shell project
ng add @angular/material --project=shell
```
- Selected theme: **Azure/Blue**
- Automatically installed: `@angular/material@20.2.2`, `@angular/cdk`, `@angular/animations`
- Updated: `projects/shell/src/styles.scss`, `projects/shell/src/index.html`

### 5. Add Angular Material to Products Project
```bash
# Add Angular Material to the products project
ng add @angular/material --project=products
```
- Selected theme: **Azure/Blue**
- Dependencies already installed (shared across workspace)
- Updated: `projects/products/src/styles.scss`, `projects/products/src/index.html`

## Micro-Frontend Setup

### 6. Install Native Federation
```bash
# Install Angular Architects Native Federation for micro-frontend support
npm i -D @angular-architects/native-federation
```
- Installed 47 packages for micro-frontend module federation
- Enables dynamic loading of micro-frontends at runtime
- Alternative to Webpack Module Federation with better Angular integration

### 7. Configure Shell as Dynamic Host
```bash
# Configure the shell project as a dynamic host
ng g @angular-architects/native-federation:init --project shell --port 4200 --type dynamic-host
```
- Switched to esbuild application builder
- Created: `projects/shell/public/federation.manifest.json`
- Created: `projects/shell/federation.config.js`
- Created: `projects/shell/src/bootstrap.ts`
- Updated: `angular.json`, `package.json`, `projects/shell/src/main.ts`

## Project Structure

After running these commands, the workspace structure includes:

```
ui-angular/
├── angular.json
├── package.json
├── projects/
│   ├── shell/
│   │   ├── src/
│   │   ├── tsconfig.app.json
│   │   └── tsconfig.spec.json
│   └── products/
│       ├── src/
│       ├── tsconfig.app.json
│       └── tsconfig.spec.json
└── ...
```

## What's Configured

### Angular Material Features:
- ✅ Azure/Blue theme applied to both projects
- ✅ Roboto font and Material Icons added
- ✅ Animation support enabled
- ✅ Global Material styles imported
- ✅ Ready to use Material components

### Next Steps:
1. Configure micro-frontend routing between shell and products
2. Set up module federation (if needed)
3. Implement shared component library
4. Add Material components to applications