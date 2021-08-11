import {Component, OnInit} from "@angular/core";
import {Account} from "../../../core/auth/account.model";
import {LoginService} from "../../../login/login.service";
import {AccountService} from "../../../core/auth/account.service";
import {ProfileService} from "../../../layouts/profiles/profile.service";
import {Router} from "@angular/router";
import {BespokeNavigationService} from "../service/bespoke-navigation.service";
import {VERSION} from "../../../app.constants";

@Component({
  selector: 'gha-fixed-assets-menu',
  templateUrl: "./fixed-assets-menu.component.html"
})
export class FixedAssetsMenuComponent implements OnInit {
  inProduction?: boolean;
  // hidden by default
  isNavbarCollapsed = true;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;

  constructor(
    private loginService: LoginService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router,
    private menuService: BespokeNavigationService
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION;
    }

    this.menuService.fixedAssetsMenuCollapseState().subscribe(state => {
      this.isNavbarCollapsed = state;
    });
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  collapseNavbar(): void {
    // this.isNavbarCollapsed = true;
    this.menuService.collapseFixedAssetsMenu()
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }
}
