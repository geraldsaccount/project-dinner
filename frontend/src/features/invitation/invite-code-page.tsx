import PageHeader from "@/components/shared/page-header";
import { useNavigate } from "react-router-dom";
import InviteCodeFormComponent from "./components/invite-code-form";

const InviteCodePage = () => {
  const navigate = useNavigate();
  function handleSubmit(code: string) {
    navigate(`/api/invite/${code}`);
  }
  return (
    <div>
      <PageHeader title="Find Your Invitation" />
      <InviteCodeFormComponent onSubmit={handleSubmit} />
    </div>
  );
};

export default InviteCodePage;

export const InviteCodeForm = InviteCodeFormComponent;
