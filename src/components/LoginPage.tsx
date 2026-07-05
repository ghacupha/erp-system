"use client";
import React, { useState } from "react";
import { Eye, EyeOff, Lock, User } from "lucide-react";
import  logo  from "../../public/favicon.ico"
// ─────────────────────────────────────────────────────────────────────────────
// CREDENTIALS — add / remove users here directly in this file.
// Nobody can log in unless their username:password is listed below.
// ─────────────────────────────────────────────────────────────────────────────
const CREDENTIALS: Record<string, string> = {
  admin:   "erp2025",
  // لإضافة مستخدم جديد، أضف سطرًا هنا:
  // ahmed:  "mypassword",
};
// ─────────────────────────────────────────────────────────────────────────────

export default function LoginPage({ onLogin }: { onLogin: () => void }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPwd,  setShowPwd]  = useState(false);
  const [error,    setError]    = useState("");
  const [loading,  setLoading]  = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    // Small artificial delay so brute-force is harder
    await new Promise(r => setTimeout(r, 400));
    const expected = CREDENTIALS[username.trim().toLowerCase()];
    if (expected && expected === password) {
      sessionStorage.setItem("erp-auth", "1");
      onLogin();
    } else {
      setError("اسم المستخدم أو كلمة المرور غير صحيحة");
      setLoading(false);
    }
  };

  return (
    <div
      className="min-h-screen flex items-center justify-center p-4"
      style={{ background: "linear-gradient(135deg, #0f172a 0%, #1e3a5f 50%, #0f172a 100%)" }}
    >
      <div className="w-full max-w-sm">
        {/* Logo */}
        <div className="flex flex-col items-center mb-8 gap-3">
          <div className="w-16 h-16 rounded-2xl bg-gradient-to-br  flex items-center justify-center shadow-xl">
            <img src={logo.src} alt="" />
          </div>
          <div className="text-center">
            <h1 className="text-white text-2xl font-700">ERP System</h1>
            <p className="text-slate-400 text-sm mt-1">Dr. Ahmed Said</p>
          </div>
        </div>

        {/* Card */}
        <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-2xl overflow-hidden">
          <div className="bg-gradient-to-r from-blue-600 to-blue-500 px-6 py-4">
            <p className="text-white font-600 text-sm flex items-center gap-2">
              <Lock size={14} /> تسجيل الدخول
            </p>
          </div>

          <form onSubmit={handleSubmit} className="p-6 space-y-4">
            {/* Username */}
            <div>
              <label className="block text-xs font-600 text-slate-500 dark:text-slate-400 mb-1.5">
                اسم المستخدم
              </label>
              <div className="relative">
                <User size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input
                  type="text"
                  autoComplete="username"
                  autoFocus
                  value={username}
                  onChange={e => { setUsername(e.target.value); setError(""); }}
                  className="erp-input pr-9"
                  placeholder="أدخل اسم المستخدم"
                  required
                />
              </div>
            </div>

            {/* Password */}
            <div>
              <label className="block text-xs font-600 text-slate-500 dark:text-slate-400 mb-1.5">
                كلمة المرور
              </label>
              <div className="relative">
                <Lock size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input
                  type={showPwd ? "text" : "password"}
                  autoComplete="current-password"
                  value={password}
                  onChange={e => { setPassword(e.target.value); setError(""); }}
                  className="erp-input pr-9 pl-9"
                  placeholder="أدخل كلمة المرور"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowPwd(v => !v)}
                  className="absolute left-9 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
                  aria-label="إظهار / إخفاء كلمة المرور"
                >
                  {showPwd ? <EyeOff size={15} /> : <Eye size={15} />}
                </button>
              </div>
            </div>

            {/* Error */}
            {error && (
              <p className="text-red-500 text-xs bg-red-50 dark:bg-red-900/20 rounded-lg px-3 py-2 border border-red-200 dark:border-red-800">
                ⚠️ {error}
              </p>
            )}

            {/* Submit */}
            <button
              type="submit"
              disabled={loading || !username || !password}
              className="erp-btn w-full justify-center bg-blue-500 hover:bg-blue-600 text-white disabled:opacity-50 py-3 text-base"
            >
              {loading ? (
                <span className="flex items-center gap-2 justify-center">
                  <svg className="animate-spin h-4 w-4" viewBox="0 0 24 24" fill="none">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"/>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"/>
                  </svg>
                  جارٍ التحقق...
                </span>
              ) : "دخول →"}
            </button>
          </form>
        </div>

       
      </div>
    </div>
  );
}
