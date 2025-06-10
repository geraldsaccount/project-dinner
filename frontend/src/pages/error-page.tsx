import PageHeader from "@/components/shared/page-header";
import { ReactNode } from "react";

type Props = {
  message: string;
  children?: ReactNode;
};

const ErrorPage = ({ message, children }: Props) => {
  return (
    <div className="flex flex-col gap-4">
      <PageHeader title="Something went wrong" />
      <div className=" text-destructive font-semibold py-4">{message}</div>
      {children}
    </div>
  );
};

export default ErrorPage;
