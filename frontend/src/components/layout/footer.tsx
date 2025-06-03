import { footerItems } from "@/data/navigation-data";
import { Link } from "react-router-dom";

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="hidden md:block w-full border-t py-4 bg-background">
      <div className="container mx-auto flex flex-col md:flex-row justify-between items-center md:px-8 px-4">
        <div className="text-sm text-muted-foreground">
          © {currentYear} Killinary. All rights reserved.
        </div>
        <nav className="flex gap-4 mt-2 md:mt-0">
          {footerItems.map((item) => (
            <Link
              key={item.title}
              to={item.to}
              className="text-sm text-muted-foreground hover:text-foreground"
            >
              {item.title}
            </Link>
          ))}
        </nav>
      </div>
    </footer>
  );
};

export default Footer;
