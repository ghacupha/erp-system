import {Component, OnInit} from "@angular/core";
import {Account} from "../../../core/auth/account.model";
import {LoginService} from "../../../login/login.service";
import {AccountService} from "../../../core/auth/account.service";
import {ProfileService} from "../../../layouts/profiles/profile.service";
import {Router} from "@angular/router";
import {VERSION} from "../../../app.constants";
import {BespokeNavigationService} from "../service/bespoke-navigation.service";


@Component({
  selector: "gha-main-menu",
  templateUrl: "./main-menu.component.html"
})
export class MainMenuComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = false;
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
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  togglePayments(): void {
    this.menuService.showPaymentsMenu();
  }

  toggleFixedAssets(): void {
    this.menuService.showFixedAssetsMenu();
  }
}
