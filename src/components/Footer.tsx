import React from "react";
import { Phone, Github, Linkedin, Mail } from "lucide-react";

export default function Footer() {
  return (
    <footer className="w-full py-8 px-4 sm:px-6 lg:px-8 border-t border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900/50 transition-colors duration-200">
      <div className="max-w-7xl mx-auto">
        <div className="flex flex-col sm:flex-row items-center justify-between gap-6 sm:gap-8">
          
          {/* Developer info */}
          <div className="flex flex-col sm:flex-row items-center gap-3 text-center sm:text-right flex-1">
            <div className="w-10 h-10 rounded-lg bg-gradient-to-br from-blue-500/10 to-purple-500/10 dark:from-blue-400/20 dark:to-purple-400/20 flex items-center justify-center flex-shrink-0">
              <img src="/mostafa.ico" alt="Mostafa Bahaa" className="w-10 h-10 rounded" />
            </div>
            <div className="flex flex-col">
              <p className="text-xs font-600 text-slate-500 dark:text-slate-400 uppercase tracking-wide">
                developed by
                
              </p>
              <p className="text-sm font-600 text-slate-700 dark:text-slate-200">
                <a
                  href="https://mostafa-s-portfolio.vercel.app/"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-blue-600 dark:text-blue-400 hover:text-blue-700 dark:hover:text-blue-300 transition-colors duration-200 underline"
                >
                  Mostafa Bahaa
                </a>
              </p>
            </div>
          </div>

          {/* Contact & Links */}
          <div className="flex flex-col sm:flex-row items-center gap-4 sm:gap-6">
            {/* Phone */}
            <div className="flex items-center gap-2.5 px-4 py-2.5 rounded-lg bg-slate-50 dark:bg-slate-800/50 border border-slate-100 dark:border-slate-700 hover:bg-slate-100 dark:hover:bg-slate-700/50 transition-colors duration-200">
              <Phone size={16} className="text-blue-500 dark:text-blue-400 flex-shrink-0" />
              <a
                href="tel:+201007809902"
                className="text-sm font-500 text-slate-700 dark:text-slate-200 dir-ltr hover:text-blue-600 dark:hover:text-blue-400 transition-colors duration-200"
              >
                01007809902
              </a>
            </div>

            {/* Social Links */}
            <div className="flex items-center gap-3">
              <a
                href="https://github.com/mostafaBahaa97"
                target="_blank"
                rel="noopener noreferrer"
                className="w-9 h-9 rounded-lg bg-slate-100 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 flex items-center justify-center hover:bg-blue-100 dark:hover:bg-blue-900/30 hover:border-blue-300 dark:hover:border-blue-600 transition-all duration-200"
                title="GitHub"
              >
                <Github size={16} className="text-slate-600 dark:text-slate-400 hover:text-blue-600 dark:hover:text-blue-400" />
              </a>
              <a
                href="https://www.linkedin.com/in/mostafabahaaeldin-dev/"
                target="_blank"
                rel="noopener noreferrer"
                className="w-9 h-9 rounded-lg bg-slate-100 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 flex items-center justify-center hover:bg-blue-100 dark:hover:bg-blue-900/30 hover:border-blue-300 dark:hover:border-blue-600 transition-all duration-200"
                title="LinkedIn"
              >
                <Linkedin size={16} className="text-slate-600 dark:text-slate-400 hover:text-blue-600 dark:hover:text-blue-400" />
              </a>
              <a
                href="mailto:mostafabahaa899@gmail.com"
                className="w-9 h-9 rounded-lg bg-slate-100 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 flex items-center justify-center hover:bg-blue-100 dark:hover:bg-blue-900/30 hover:border-blue-300 dark:hover:border-blue-600 transition-all duration-200"
                title="Email"
              >
                <Mail size={16} className="text-slate-600 dark:text-slate-400 hover:text-blue-600 dark:hover:text-blue-400" />
              </a>
            </div>
          </div>
        </div>

        {/* Bottom divider and copyright */}
        <div className="mt-6 pt-6 border-t border-slate-100 dark:border-slate-800">
          <p className="text-xs text-slate-500 dark:text-slate-400 text-center">
            © 2026 نظام ERP الإدارة المتكاملة. جميع الحقوق محفوظة.
          </p>
        </div>
      </div>
    </footer>
  );
}
