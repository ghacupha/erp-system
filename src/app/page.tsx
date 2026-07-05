"use client";
import React, { useState, useEffect, useCallback } from "react";
import { Sidebar, TopBar, ModuleId } from "@/components/layout";
import { Toast } from "@/components/ui";
import DashboardModule    from "@/components/DashboardModule";
import DefinitionsModule  from "@/components/DefinitionsModule";
import TransactionsModule from "@/components/TransactionsModule";
import StatementsModule   from "@/components/StatementsModule";
import FinanceModule      from "@/components/FinanceModule";
import Footer             from "@/components/Footer";
import InstallPrompt      from "@/components/InstallPrompt";
import LoginPage          from "@/components/LoginPage";
import SplashScreen       from "@/components/SplashScreen";

interface ToastState { id: number; message: string; type: "success" | "error"; }

export default function Home() {
  const [authed,      setAuthed]      = useState<boolean | null>(null); // null = checking
  const [active,      setActive]      = useState<ModuleId>("dashboard");
  const [dark,        setDark]        = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [toasts,      setToasts]      = useState<ToastState[]>([]);

  // Resolve auth state on mount
  useEffect(() => {
    const saved = localStorage.getItem("erp-dark");
    if (saved === "true") setDark(true);
    // Small tick so SplashScreen renders at least one frame before resolving
    setTimeout(() => {
      setAuthed(sessionStorage.getItem("erp-auth") === "1");
    }, 1200);
  }, []);

  useEffect(() => {
    document.documentElement.classList.toggle("dark", dark);
    localStorage.setItem("erp-dark", String(dark));
  }, [dark]);

  const showToast = useCallback((message: string, type: "success" | "error" = "success") => {
    const id = Date.now();
    setToasts(prev => [...prev, { id, message, type }]);
  }, []);

  const removeToast = useCallback((id: number) => {
    setToasts(prev => prev.filter(t => t.id !== id));
  }, []);

  const handleLogout = () => {
    sessionStorage.removeItem("erp-auth");
    setAuthed(false);
  };

  // ── Splash screen (checking auth) ─────────────────────────────────────────
  if (authed === null) {
    return (
      <>
        <SplashScreen />
        {/* Install prompt can show above the splash too */}
        <InstallPrompt />
      </>
    );
  }

  // ── Login page ─────────────────────────────────────────────────────────────
  if (!authed) {
    return (
      <>
        <LoginPage onLogin={() => setAuthed(true)} />
        <InstallPrompt />
      </>
    );
  }

  // ── Main app ───────────────────────────────────────────────────────────────
  return (
    <div className="relative flex min-h-screen bg-slate-50 dark:bg-slate-950 transition-colors duration-300">
      {sidebarOpen && (
        <button
          onClick={() => setSidebarOpen(false)}
          className="fixed inset-0 z-30 bg-black/40 lg:hidden"
          aria-label="إغلاق القائمة"
        />
      )}

      <Sidebar
        active={active}
        onNavigate={(id) => { setActive(id); setSidebarOpen(false); }}
        dark={dark}
        onToggleDark={() => setDark(d => !d)}
        isOpen={sidebarOpen}
        onClose={() => setSidebarOpen(false)}
        onLogout={handleLogout}
      />

      <div className="flex flex-col flex-1 min-h-screen min-w-0 lg:mr-[var(--sidebar-w)]">
        <TopBar active={active} onToggleSidebar={() => setSidebarOpen(true)} />

        <main className="flex-1 p-4 sm:p-6 max-w-7xl mx-auto w-full">
          {active === "dashboard"    && <DashboardModule />}
          {active === "definitions"  && <DefinitionsModule  showToast={showToast} />}
          {active === "transactions" && <TransactionsModule showToast={showToast} />}
          {active === "statements"   && <StatementsModule />}
          {active === "finance"      && <FinanceModule      showToast={showToast} />}
        </main>

        <Footer />
      </div>

      <div className="fixed bottom-6 left-1/2 -translate-x-1/2 z-[9999] flex flex-col w-full max-w-xs gap-2 items-center pointer-events-none">
        {toasts.map(t => (
          <Toast key={t.id} message={t.message} type={t.type} onDone={() => removeToast(t.id)} />
        ))}
      </div>
    </div>
  );
}
